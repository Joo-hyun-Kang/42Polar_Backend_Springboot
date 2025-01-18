package com._polar._polar_backend_spring.v1.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationDto {
    @NotNull
    @Max(1000)
    @Min(1)
    private int take;

    @NotNull
    @Max(10000)
    @Min(1)
    private int page;
}
