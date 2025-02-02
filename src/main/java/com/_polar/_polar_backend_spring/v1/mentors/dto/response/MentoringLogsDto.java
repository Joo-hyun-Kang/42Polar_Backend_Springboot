package com._polar._polar_backend_spring.v1.mentors.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MentoringLogsDto {
    private String id;
    private LocalDateTime createdAt;
    private List<LocalDateTime> meetingAt;

    private CadetDto cadet;
    private String topic;
    private String status;
    private ReportDto report;
    private MetaDto meta;

    @Data
    public static class CadetDto {
        private String name;
        private String intraId;
        private String resumeUrl;
        @JsonProperty("isCommon")
        private boolean isCommon;
    }

    @Data
    public static class ReportDto {
        private String id;
        private String status;
    }

    @Data
    public static class MetaDto {
        private List<List<LocalDateTime>> requestTime;
        private String rejectMessage;
        private String content;
    }
}
