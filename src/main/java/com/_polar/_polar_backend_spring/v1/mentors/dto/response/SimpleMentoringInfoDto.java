package com._polar._polar_backend_spring.v1.mentors.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SimpleMentoringInfoDto {
    private List<SimpleLogDto> logs;
    private int total;
}
