package com._polar._polar_backend_spring.v1.redis;

import com._polar._polar_backend_spring.v1.auth.dto.request.CursesUser42OriginDto;
import com._polar._polar_backend_spring.v1.auth.dto.request.UserInfo42OriginDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginHandler {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String LOGIN_QUEUE = "loginQueue";
    private final Environment env;

    public UserInfo42OriginDto handler(String accessToken) {
        // リトライ設定
        int maxRetries = 3; // 最大リトライ回数
        int interval = 500; // リトライ間隔 (ミリ秒)

        for (int i = 0; i < maxRetries; i++) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Redisログインキュー失敗", e);

                return null;
            }

            redisTemplate.opsForList().leftPush(LOGIN_QUEUE, accessToken);
            UserInfo42OriginDto userInfo42OriginDto =  processQueue();

            if (userInfo42OriginDto != null) {
                return userInfo42OriginDto;
            }
        }

        return null;
    }

    private UserInfo42OriginDto processQueue() {
        try {
            String accessToken = redisTemplate.opsForList().rightPop(LOGIN_QUEUE);

            return handleJob(accessToken);
        } catch (Exception e) {
            log.error("Redisログインキュー失敗", e);

            return null;
        }
    }

    private UserInfo42OriginDto handleJob(String accessToken) {
        final RestTemplate restTemplate = new RestTemplate();
        final ObjectMapper objectMapper = new ObjectMapper();

        String resourceServerUrl = env.getProperty("RESOURCE_SERVER_42");

        try {
            // Authorizationヘッダーの設定
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            // REST APIコール
            ResponseEntity<String> response = restTemplate.exchange(
                    resourceServerUrl, HttpMethod.GET, requestEntity, String.class);

            JsonNode responseJson = objectMapper.readTree(response.getBody());

            // Curses情報を DTO に変換
            List<CursesUser42OriginDto> curses = new ArrayList<>();
            JsonNode cursesUsersNode = responseJson.get("cursus_users");

            if (cursesUsersNode != null && cursesUsersNode.isArray()) {
                for (JsonNode node : cursesUsersNode) {
                    CursesUser42OriginDto dto = new CursesUser42OriginDto();

                    dto.setId(node.get("id").asInt());
                    dto.setBeginAt(node.get("begin_at").asText());
                    dto.setEndAt(node.hasNonNull("end_at") ? node.get("end_at").asText() : null);
                    dto.setGrade(node.hasNonNull("grade") ? node.get("grade").asText() : null);
                    dto.setLevel(node.get("level").asDouble());
                    dto.setBlackholedAt(node.hasNonNull("blackholed_at") ? node.get("blackholed_at").asText() : null);

                    curses.add(dto);
                }
            }

            return new UserInfo42OriginDto(
                    responseJson.get("id").asInt(),
                    responseJson.get("email").asText(),
                    responseJson.get("login").asText(),
                    responseJson.get("image").get("link").asText(),
                    curses,
                    responseJson.get("staff?").asBoolean()
            );
        } catch (Exception e) {
            log.error("OAuthサーバーからリソスを取得するのに失敗しました", e);
            return null;
        }
    }
}
