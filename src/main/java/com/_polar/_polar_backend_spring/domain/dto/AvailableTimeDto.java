package com._polar._polar_backend_spring.domain.dto;

import lombok.Data;

@Data
public class AvailableTimeDto {
    private Integer startHour;
    private Integer startMinute;
    private Integer endHour;
    private Integer endMinute;
}
