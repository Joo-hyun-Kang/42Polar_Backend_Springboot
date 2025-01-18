package com._polar._polar_backend_spring.domain.convertor;

import com._polar._polar_backend_spring.domain.entity.enums.LogStatus;
import jakarta.persistence.AttributeConverter;

public class LogStatusConverter implements AttributeConverter<LogStatus, String> {
    @Override
    public String convertToDatabaseColumn(LogStatus logStatus) {
        if (logStatus == null) {
            return null;
        }

        return logStatus.getDescription(); // 日本語の値を保存
    }

    @Override
    public LogStatus convertToEntityAttribute(String dbData) {
        for (LogStatus status : LogStatus.values()) {
            if (status.getDescription().equals(dbData)) {
                return status;
            }
        }

        assert false : "LogStatusがデータベースに存在できません。";
        return null;
    }
}