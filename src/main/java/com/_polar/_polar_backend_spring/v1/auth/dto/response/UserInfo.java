package com._polar._polar_backend_spring.v1.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfo {
    private String intraId;
    private String role;
    private boolean join;
}