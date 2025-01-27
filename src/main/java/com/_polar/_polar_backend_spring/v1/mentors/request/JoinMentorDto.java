package com._polar._polar_backend_spring.v1.mentors.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class JoinMentorDto {
    @NotNull
    @NotBlank()
    private String name;

    @NotNull
    @NotBlank()
    private String slackId;

    private List<List<AvailableTimeDto>> availableTime;

    @NotNull()
    private Boolean isActive;

    @NotNull
    @NotBlank()
    private String company;

    @NotNull
    @NotBlank()
    private String duty;
}