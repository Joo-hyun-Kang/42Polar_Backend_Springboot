package com._polar._polar_backend_spring.domain.entity.enums;

public enum LogStatus {
    WATING("お待ち中"),
    CANCEL("取消"),
    CONFIRMED("確定"),
    DONE("完了");

    private final String description;

    LogStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
