package com._polar._polar_backend_spring.v1.auth.interceptors;

import com._polar._polar_backend_spring.v1.auth.JwtHandler;
import com._polar._polar_backend_spring.v1.auth.decorators.AuthGuard;
import com._polar._polar_backend_spring.v1.auth.dto.common.AuthInfo;
import com._polar._polar_backend_spring.v1.auth.enums.ROLES;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.file.AccessDeniedException;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtHandler jwtHandler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod hm) {
            AuthGuard authGuardAnnotation = hm.getMethodAnnotation(AuthGuard.class);

            if (authGuardAnnotation != null) {
                String token = extractTokenOrNull(request);
                if (token == null) {
                    throw new AccessDeniedException("Unauthorized: No JWT token provided");
                }

                //正しいJWTトークンを持っているか検証
                Claims claims = null;
                try {
                    claims = jwtHandler.parseToken(token);

                    String id = (String) claims.get("sub");
                    String role = (String) claims.get("role");
                    String intraId = (String) claims.get("username");

                    if (id == null || role == null || intraId == null) {
                        throw new AccessDeniedException("正しいトークンを持っていません。");
                    }

                    //AuthInfoResolverに使用
                    request.setAttribute("authInfo", new AuthInfo(id, role, intraId));
                } catch (Exception e) {
                    throw new AccessDeniedException("正しいトークンを持っていません。");
                }

                //Roleに応じてコントローラーに接近制限
                ROLES[] rolesOfPermission = authGuardAnnotation.value();
                if (rolesOfPermission.length > 0) {
                    String roleFromToken = extractRolesOrNull(claims);
                    if (roleFromToken == null) {
                        throw new AccessDeniedException("正しいトークン形ではありません。：Roleを持っていないリクエスト");
                    }

                    if (!isRolesPremised(roleFromToken, rolesOfPermission)) {
                        throw new AccessDeniedException("接近可能な役割を持っておりません。");
                    }
                }
            }
        }

        return true;
    }

    private String extractRolesOrNull(Claims claims) {
        try {
            final String ROLE_KEY_NAME = "role";
            Object roleName = claims.get(ROLE_KEY_NAME);
            return roleName.toString();
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }

    private boolean isRolesPremised(String roleFromToken, ROLES[] rolesOfPermission) {
        boolean isRoleIn = false;
        for (ROLES roles : rolesOfPermission) {
            if (roles.getRole().equals(roleFromToken)) {
                isRoleIn = true;
                break;
            }
        }

        return isRoleIn;
    }

    private String extractTokenOrNull(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        final String API_TOKEN_HEADER_NAME = "Authorization";
        String authorizationHeader = request.getHeader(API_TOKEN_HEADER_NAME);

        //ユーザーがトークンを持っていないまま、リクエスト
        if (cookies == null && authorizationHeader == null) {
            return null;
        }

        // プロントでCookieおよびHeaderにJwtToken載せられてくるケースがあり
        if (cookies != null) {
            return parseCookies(cookies);
        } else {
            return extractTokenFromHeader(authorizationHeader);
        }
    }

    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader.startsWith("Bearer ") || authorizationHeader.startsWith("bearer ") ) {
            return authorizationHeader.substring(7);
        }

        return null;
    }

    private String parseCookies(Cookie[] cookies) {
        final String API_TOKEN_COOKIE_NAME = "access_token";

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