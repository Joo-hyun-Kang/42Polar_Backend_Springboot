package com._polar._polar_backend_spring.v1.mentors.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateMentorDto {
    private String name;
    private String slackId;
    private List<List<AvailableTimeDto>> availableTime;
    private Boolean isActive;
    private String company;
    private String duty;
    private String introduction;
    private ArrayList<@NotNull @NotBlank String> tags;
    private String markdownContent;
}
