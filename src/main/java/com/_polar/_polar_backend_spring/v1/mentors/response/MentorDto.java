package com._polar._polar_backend_spring.v1.mentors.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class MentorDto {
    private String id;
    private String intraId;
    private String slackId;
    private String name;
    private String email;
    private String company;
    private String duty;
    private String profileImage;
    private String availableTime;
    private String introduction;
    private List<String> tags;
    @JsonProperty("isActive")
    private boolean isActive;
    private String markdownContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}