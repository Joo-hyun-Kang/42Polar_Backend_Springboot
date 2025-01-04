package com._polar._polar_backend_spring.v1.redis;

import com._polar._polar_backend_spring.v1.auth.dto.request.CursesUser42OriginDto;
import com._polar._polar_backend_spring.v1.auth.dto.request.UserInfo42OriginDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginConsumer implements MessageListener {
    private final Environment env;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        final RestTemplate restTemplate = new RestTemplate();
        final ObjectMapper objectMapper = new ObjectMapper();

        String accessToken = new String(message.getBody());
        String resourceServerUrl = env.getProperty("RESOURCE_SERVER_42");

        // HttpHeadersを使ってAuthorizationヘッダーを設定
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            // GETリクエストを送信
            ResponseEntity<String> response = restTemplate.exchange(
                    resourceServerUrl,        // URL
                    HttpMethod.GET,           // HTTPメソッド
                    requestEntity,            // リクエストエンティティ（ヘッダー含む）
                    String.class
            );

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

            // ユーザー情報を DTO に変換
            UserInfo42OriginDto userInfo = new UserInfo42OriginDto(
                    responseJson.get("id").asInt(),
                    responseJson.get("email").asText(),
                    responseJson.get("login").asText(),
                    responseJson.get("image").get("link").asText(),
                    curses,
                    responseJson.get("staff").asText()
            );

            String userInfoJson = objectMapper.writeValueAsString(userInfo);
            redisTemplate.opsForValue().set(accessToken, userInfoJson);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "OAuthサーバーからリソスを取得するのに失敗しました", e);
        }
    }
}
