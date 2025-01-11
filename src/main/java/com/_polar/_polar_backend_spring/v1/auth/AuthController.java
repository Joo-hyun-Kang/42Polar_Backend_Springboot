package com._polar._polar_backend_spring.v1.auth;

import com._polar._polar_backend_spring.v1.auth.dto.request.UserInfo42OriginDto;
import com._polar._polar_backend_spring.v1.auth.dto.response.AuthResponse;
import com._polar._polar_backend_spring.v1.auth.dto.response.JwtInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtHandler jwtHandler;
    private final Environment env;

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

        JwtInfo jwtInfo = authService.createAndUpdateProfile(userProfile);
        if (jwtInfo == null) {
            throw new AccessDeniedException("42cursusに属している方しか利用できます。");
        }

        String jwtToken = jwtHandler.sign(userProfile.getId().toString(), jwtInfo.getIntraId(), jwtInfo.getRole());

        return new AuthResponse(jwtToken, jwtInfo);
    }
}
