package com._polar._polar_backend_spring.v1.mentors.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class SimpleLogDto {
    private String id;
    private List<LocalDateTime> meetingAt;
    private LocalDateTime meetingStart;
    private String topic;
    private String status;
    private LocalDateTime createdAt;
}
