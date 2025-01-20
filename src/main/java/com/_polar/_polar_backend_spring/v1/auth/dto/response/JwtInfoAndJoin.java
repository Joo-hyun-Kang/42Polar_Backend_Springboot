package com._polar._polar_backend_spring.v1.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtInfoAndJoin {
    private JwtInfo jwtInfo;
    @JsonProperty("isJoined")
    private boolean isJoined;
}
