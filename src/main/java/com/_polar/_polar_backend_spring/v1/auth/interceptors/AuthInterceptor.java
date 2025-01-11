package com._polar._polar_backend_spring.v1.auth.interceptors;

import com._polar._polar_backend_spring.v1.auth.JwtHandler;
import com._polar._polar_backend_spring.v1.auth.decorators.AuthGuard;
import com._polar._polar_backend_spring.v1.auth.decorators.Roles;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtHandler jwtHandler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;

            AuthGuard isAuthGuard = hm.getMethodAnnotation(AuthGuard.class);

            if (isAuthGuard != null) {
                String token = extractTokenOrNull(request);
                if (token == null) {
                    throw new AccessDeniedException("Unauthorized: No JWT token provided");
                }

                try {
                    Claims claims = jwtHandler.parseToken(token);
                    request.setAttribute("user", claims);
                } catch (
                        Exception e) {
                    throw new AccessDeniedException("正しいトークンを持っていません。");
                }
            }
        }
        return true;
    }

    private String extractTokenOrNull(HttpServletRequest request) {
        final String API_TOKEN_COOKIE_NAME = "access_token";
        final String API_TOKEN_HEADER_NAME = "Authorization";

        Cookie[] cookies = request.getCookies();
        String authorizationHeader = request.getHeader(API_TOKEN_HEADER_NAME);

        //ユーザーがトークンを持っていないまま、リクエスト
        if (cookies == null && authorizationHeader == null) {
            return null;
        }

        if (cookies != null) {
            return parseCookies(API_TOKEN_COOKIE_NAME, cookies);
        } else {
            return extractToken(authorizationHeader);
        }
    }

    private static String extractToken(String authorizationHeader) {
        if (authorizationHeader.startsWith("Bearer ") || authorizationHeader.startsWith("bearer ") ) {
            return authorizationHeader.substring(7);
        }

        return null;
    }

    private static String parseCookies(String API_TOKEN_COOKIE_NAME, Cookie[] cookies) {
        boolean isTokenIn = false;
        String token = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(API_TOKEN_COOKIE_NAME)) {
                token = cookie.getValue();
                isTokenIn = true;
            }
        }

        return isTokenIn ? token : null;
    }
}