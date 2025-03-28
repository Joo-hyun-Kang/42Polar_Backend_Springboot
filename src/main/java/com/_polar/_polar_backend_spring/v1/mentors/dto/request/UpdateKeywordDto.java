package com._polar._polar_backend_spring.v1.mentors.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateKeywordDto {
    private List<String> keywords;
}
