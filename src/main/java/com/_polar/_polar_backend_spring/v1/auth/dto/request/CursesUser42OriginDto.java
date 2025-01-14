package com._polar._polar_backend_spring.v1.auth.dto.request;

import lombok.Data;

@Data
public class CursesUser42OriginDto {
    private Integer id;
    private String beginAt;
    private String endAt;
    private String grade;
    private Double level;
    private String blackholedAt;
}
