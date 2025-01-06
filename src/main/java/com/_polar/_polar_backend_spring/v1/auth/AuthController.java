package com._polar._polar_backend_spring.v1.auth;

import com._polar._polar_backend_spring.v1.auth.dto.request.UserInfo42OriginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
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
    public String signIn42Intra(@RequestParam("code") String authCode) {
        // 42APIからユーザー情報を取得
        UserInfo42OriginDto userProfile = authService.getProfileBy42Intra(authCode);
        if (userProfile == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "OAuthサーバーからリソスを取得するのに失敗しました");
        }

        // ユーザの情報バーリデーションと最初ローグインの場合DBに登録する
//        var jwtAndJoin = authService.createAndUpdateProfile(userProfile);
//
//        // JWT トークンを生成
//        String jwt = jwtService.createToken(
//                jwtAndJoin.getJwtInfo().getId(),
//                jwtAndJoin.getJwtInfo().getIntraId(),
//                jwtAndJoin.getJwtInfo().getRole()
//        );

//        // レスポンスを作成
//        return new AuthResponse(
//                jwt,
//                jwtAndJoin.getJwtInfo().getIntraId(),
//                jwtAndJoin.getJwtInfo().getRole(),
//                jwtAndJoin.isJoined()
//        );

        return "Developing";
    }
}
