package com._polar._polar_backend_spring.v1.categories.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class MentorKeywordsDto {
    private List<String> keywords;
    private String mentorName;
}
