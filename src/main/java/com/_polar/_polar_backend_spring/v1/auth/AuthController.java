package com._polar._polar_backend_spring.v1.auth;

import com._polar._polar_backend_spring.v1.auth.decorators.AuthGuard;
import com._polar._polar_backend_spring.v1.auth.decorators.Roles;
import com._polar._polar_backend_spring.v1.auth.dto.request.UserInfo42OriginDto;
import com._polar._polar_backend_spring.v1.auth.dto.response.AuthResponse;
import com._polar._polar_backend_spring.v1.auth.dto.response.JwtInfo;
import com._polar._polar_backend_spring.v1.auth.dto.response.JwtInfoAndJoin;
import com._polar._polar_backend_spring.v1.auth.dto.response.UserInfo;
import com._polar._polar_backend_spring.v1.auth.enums.ROLES;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final JwtHandler jwtHandler;
    private final Environment env;

    @AuthGuard()
    @Roles({ROLES.MENTOR, ROLES.CADET})
    @GetMapping("api/v1/interceptor")
    public String validation() throws AccessDeniedException {
        log.info("validation");
//        throw new AccessDeniedException("aaa");
        return "validation";
    }

//    @GetMapping("/interceptor")
//    public String validation(HttpServletRequest request) {
//        Claims user = (Claims) request.getAttribute("user");
//        if (user == null) {
//            return "No user data found";
//        }
//
//        String username = user.get("username", String.class);
//        String role = user.get("role", String.class);
//
//        return String.format("Authenticated user: %s, Role: %s", username, role);
//    }



    @GetMapping("api/v1/login")
    public String login() {
        String clientId = env.getProperty("UID_42");
        String redirectUri = env.getProperty("REDIRECT_42");

        return String.format(
                "https://api.intra.42.fr/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code",
                clientId,
                redirectUri
        );
    }

    @GetMapping("/auth/oauth/callback")
    public AuthResponse signIn42Intra(@RequestParam("code") String authCode) throws AccessDeniedException {
        // 42APIからユーザー情報を取得
        UserInfo42OriginDto userProfile = authService.getProfileBy42Intra(authCode);
        if (userProfile == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "OAuthサーバーからリソスを取得するのに失敗しました");
        }

        JwtInfoAndJoin jwtInfoAndJoin = authService.createAndUpdateProfile(userProfile);
        if (jwtInfoAndJoin == null) {
            throw new AccessDeniedException("アクセスする権限がありません。");
        }

        JwtInfo jwtInfo = jwtInfoAndJoin.getJwtInfo();
        String jwtToken = jwtHandler.sign(jwtInfo.getId(), jwtInfo.getIntraId(), jwtInfo.getRole());

        return new AuthResponse(jwtToken, new UserInfo(jwtInfo.getIntraId(), jwtInfo.getRole(), jwtInfoAndJoin.isJoined()));
    }
}
