package com._polar._polar_backend_spring.v1.auth;

import com._polar._polar_backend_spring.v1.auth.dto.request.UserInfo42OriginDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final Environment env;
    private final RestTemplate restTemplate = new RestTemplate();

    public UserInfo42OriginDto getProfileBy42Intra(String authCode) {
        String providerUrl = String.format(
                "https://api.intra.42.fr/oauth/token?grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s&redirect_uri=%s",
                env.getProperty("UID_42"), env.getProperty("SECRET_42"), authCode, env.getProperty("REDIRECT_42"));

        String accessToken = getAccessToken(authCode, providerUrl);

        return null;
//        return loginProducer.addJob(tokenResponse.getAccessToken());
    }

    private String getAccessToken(String authCode, String providerUrl) {
        try {
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

            return root.get(ACCESS_TOKEN_API_FORM_NAME).toString();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "OAuthサーバーからリソスを取得するのに失敗しました", e);
        }
    }
}
