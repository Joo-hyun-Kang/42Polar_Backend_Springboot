package com._polar._polar_backend_spring.v1.cadets.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCadetDto {
    private String intraId;
    private String profileImage;
    private boolean isCommon;
    private String email;
}
