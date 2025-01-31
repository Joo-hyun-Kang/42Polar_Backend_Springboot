package com._polar._polar_backend_spring.v1.mentors.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MentoringInfoDto {
    private List<MentoringLogsDto> logs;
    private long total;
}
