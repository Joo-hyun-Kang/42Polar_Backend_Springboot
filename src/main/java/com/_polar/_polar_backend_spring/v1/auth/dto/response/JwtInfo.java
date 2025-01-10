package com._polar._polar_backend_spring.v1.auth.dto.response;

import com._polar._polar_backend_spring.v1.auth.enums.ROLES;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtInfo {
    private String id;
    private String intraId;
    private ROLES role;
}