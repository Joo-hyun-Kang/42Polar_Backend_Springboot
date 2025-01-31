package com._polar._polar_backend_spring.v1.mentors.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class MentorEnrollDto {
    private String name;
    private String slackId;
    private String availableTime;
    private Boolean isActive;
    private String company;
    private String duty;
}
