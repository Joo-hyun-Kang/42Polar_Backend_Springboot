package com._polar._polar_backend_spring.v1.auth;

import com._polar._polar_backend_spring.v1.auth.enums.ROLES;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtHandler {
    private final Environment env;

    /* jwt形
        {"alg":"HS256","typ":"JWT"}
        {"sub":"524ddd94-8995-44a9-8a0a-f683503716f3","username":"jokang","role":"cadet","iat":1726224873,"exp":1726311273}
        ߯+bkK]TpD2x
     */
    public String sign(String id, String intraId, String roleToString) {
        final String JWT_SECRET = env.getProperty("JWT_SECRET");
        final long JWT_EXPIRE_ONE_DAY = Long.parseLong(env.getProperty("JWT_EXPIRE"));

        // クレーム（ペイロード）を設定
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", intraId);
        claims.put("role", roleToString);

        Date now = new Date();

        // トークン生成
        return Jwts.builder()
                .setClaims(claims) // カスタムクレーム
                .setSubject(id) // 主題（例: ユーザーID）
                .setIssuedAt(now) // 発行時刻
                .setExpiration(new Date(now.getTime() + JWT_EXPIRE_ONE_DAY)) // 有効期限
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET) // HS256アルゴリズムと秘密鍵で署名
                .compact();
    }
}
