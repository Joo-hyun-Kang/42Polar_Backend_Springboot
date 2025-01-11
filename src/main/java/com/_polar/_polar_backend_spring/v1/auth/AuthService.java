package com._polar._polar_backend_spring.v1.auth;

import com._polar._polar_backend_spring.domain.entity.Bocals;
import com._polar._polar_backend_spring.domain.entity.Cadets;
import com._polar._polar_backend_spring.domain.entity.Mentors;
import com._polar._polar_backend_spring.v1.auth.dto.request.CursesUser42OriginDto;
import com._polar._polar_backend_spring.v1.auth.dto.request.UserInfo42OriginDto;
import com._polar._polar_backend_spring.v1.auth.dto.response.AuthResponse;
import com._polar._polar_backend_spring.v1.auth.dto.response.JwtInfo;
import com._polar._polar_backend_spring.v1.auth.enums.ROLES;
import com._polar._polar_backend_spring.v1.bocals.BocalsService;
import com._polar._polar_backend_spring.v1.cadets.CadetsService;
import com._polar._polar_backend_spring.v1.cadets.dto.common.CreateCadetDto;
import com._polar._polar_backend_spring.v1.mentors.MentorsService;
import com._polar._polar_backend_spring.v1.redis.LoginHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final Environment env;
    private final LoginHandler loginHandler;
    private final MentorsService mentorsService;
    private final BocalsService bocalsService;
    private final CadetsService cadetsService;


    public UserInfo42OriginDto getProfileBy42Intra(String authCode) {
        String providerUrl = String.format(
                "https://api.intra.42.fr/oauth/token?grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s&redirect_uri=%s",
                env.getProperty("UID_42"), env.getProperty("SECRET_42"), authCode, env.getProperty("REDIRECT_42"));

        String accessToken = getAccessToken(authCode, providerUrl);

        return loginHandler.handler(accessToken);
    }

    private String getAccessToken(String authCode, String providerUrl) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.postForEntity(providerUrl, null, String.class);
            /*　System.out.println(response);
                {
                  "status": "200 OK",
                  "data": {
                    "access_token": "c397a86b4747acf08e2efde636f9649d947d29ce53d50c7bbc85ad9299a6c567",
                    "token_type": "bearer",
                    "expires_in": 2934,
                    "refresh_token": "8ecd126c63a1914e4b8b8cce16624490caa7c14a33c902f5a807e1fca619dfea",
                    "scope": "public",
                    "created_at": 1735977709,
                    "secret_valid_until": 1738371629
                  },
                  "headers": {
                    要らないので、省略、
                  }
                }
             */

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());
            final String ACCESS_TOKEN_API_FORM_NAME = "access_token";

            return root.get(ACCESS_TOKEN_API_FORM_NAME).toString().replaceAll("^\"|\"$", "");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "OAuthサーバーからリソスを取得するのに失敗しました", e);
        }
    }

    public JwtInfo createAndUpdateProfile(UserInfo42OriginDto userProfile) {
        // 必要な情報を抽出
        String intraId = userProfile.getLogin();
        boolean isStaff = userProfile.getStaff();
        String email = userProfile.getEmail();
        List<CursesUser42OriginDto> cursus = userProfile.getCursus();
        String profileImage = userProfile.getProfileImage();

        AuthResponse authResponse = null;
        boolean isJoined = false; // 初回ログインかを確認

        if (intraId.startsWith("m-")) {
            //データベースにメンターがあるかないか確認
            Mentors mentors = null;

            if (!mentorsService.isMentor(intraId)) {
                mentors = mentorsService.createUser(intraId);
                isJoined = false;
            } else {
                mentors = mentorsService.findByIntra(intraId);
                //メンターが最初ログイン後、情報入力したか確認
                isJoined = mentors.isInitialized();
            }

            return new JwtInfo(mentors.getIntraId(), ROLES.MENTOR, isJoined);
        }

        if (isStaff) {
            isJoined = true;

            Bocals bocal = null;
            if (!bocalsService.isBocal(intraId)) {
                bocal = bocalsService.createUser(intraId);
            } else {
                bocal = bocalsService.findByIntra(intraId);
                bocalsService.updateLogin(bocal, intraId);
            }

            return new JwtInfo(bocal.getIntraId(), ROLES.BOCAL, isJoined);
        }

        if (cursus.size() < 2 || isBlackholed(cursus)) {
            log.error("42cursusに属している方しか利用できます。");
            return null;
        }

        Cadets cadet = null;
        isJoined = false;
        if (!cadetsService.isCadet(intraId)) {
            CreateCadetDto createCadetDto = new CreateCadetDto(intraId, profileImage, getGrade(cursus), email);
            cadet = cadetsService.createUser(createCadetDto);

        } else {
            CreateCadetDto updateData = new CreateCadetDto(intraId, profileImage, getGrade(cursus), email);
            cadet = cadetsService.findByIntraId(intraId);
            cadetsService.updateLogin(cadet, updateData);

            isJoined = cadet.isInitialized();
        }

        return new JwtInfo(cadet.getIntraId(), ROLES.CADET, isJoined);
    }

    private boolean getGrade(List<CursesUser42OriginDto> cursus) {
        final int COMMON_CIRCLE_INDEX = 1;

        return cursus.get(COMMON_CIRCLE_INDEX).getGrade().equals("Learner");
    }

    private boolean isBlackholed(List<CursesUser42OriginDto> cursus) {
        final int COMMON_CIRCLE_INDEX = 1;
        CursesUser42OriginDto commonCircle = cursus.get(COMMON_CIRCLE_INDEX);

        if (getGrade(cursus) && commonCircle.getEndAt() != null) {
            return true;
        }

        if (commonCircle.getBlackholedAt() != null && parseUserInfo42Date(commonCircle.getBlackholedAt()).isBefore(LocalDateTime.now())) {
            return true;
        }

        return false;
    }

    private LocalDateTime parseUserInfo42Date(String userInfoDate) {
        //入るDate形："2024-04-01T07?:38?:00.000Z"
        String formattedDate = userInfoDate.replace("?", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

        return LocalDateTime.parse(formattedDate, formatter);
    }
}
