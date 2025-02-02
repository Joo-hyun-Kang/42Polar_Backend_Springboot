package com._polar._polar_backend_spring.domain.convertor;

import com._polar._polar_backend_spring.domain.entity.enums.ReportStatus;
import jakarta.persistence.AttributeConverter;

public class ReportStatusConverter implements AttributeConverter<ReportStatus, String>  {
    @Override
    public String convertToDatabaseColumn(ReportStatus reportStatus) {
        if (reportStatus == null) {
            return null;
        }

        return reportStatus.getDescription(); // 日本語の値を保存
    }

    @Override
    public ReportStatus convertToEntityAttribute(String dbData) {
        for (ReportStatus status : ReportStatus.values()) {
            if (status.getDescription().equals(dbData)) {
                return status;
            }
        }

        assert false : "ReportStatusがデータベースに存在できません。";
        return null;
    }
}
