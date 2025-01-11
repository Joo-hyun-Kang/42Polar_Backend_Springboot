package com._polar._polar_backend_spring.v1.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtInfoAndJoin {
    private JwtInfo jwtInfo;
    private boolean isJoined;
}
