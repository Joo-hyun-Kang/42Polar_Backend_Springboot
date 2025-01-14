package com._polar._polar_backend_spring.v1.auth.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthInfo {
    private String id;
    private String intraId;
    private String role;
}
