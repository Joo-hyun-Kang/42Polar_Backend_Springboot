package com._polar._polar_backend_spring.v1.mentors.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class MentorUpdateDto {
    private String name;
    private String slackId;
    private String availableTime;
    private Boolean isActive;
    private String company;
    private String duty;
    private String introduction;
    private ArrayList<String> tags;
    private String markdownContent;
}
