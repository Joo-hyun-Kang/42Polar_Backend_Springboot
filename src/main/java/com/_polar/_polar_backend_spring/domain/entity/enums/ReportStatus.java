package com._polar._polar_backend_spring.domain.entity.enums;

public enum ReportStatus {
    UNABLE("作成不可"),
    ABLE("作成可能"),
    WRITING("作成中"),
    DONE("作成完了"),
    FIXING("修正期間"),
    ERROR("ERROR");

    private final String description;

    ReportStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
