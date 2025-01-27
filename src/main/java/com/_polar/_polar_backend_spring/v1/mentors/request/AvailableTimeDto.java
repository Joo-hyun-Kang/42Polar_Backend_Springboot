package com._polar._polar_backend_spring.v1.mentors.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AvailableTimeDto {
    @NotNull
    int startHour;

    @NotNull
    int startMinute;

    @NotNull
    int endHour;

    @NotNull
    int endMinute;
}
