package com._polar._polar_backend_spring.v1.keywords.dto.db;

import lombok.Data;

import java.util.List;

@Data
public class MentorsListRow {
    private String id;
    private String name;
    private String intraId;
    private String profileImage;
    private List<String> tags;
    private String introduction;
    private Boolean isActive;
    private String keyword;

    public MentorsListRow(String id, String name, String intraId, String profileImage, List<String> tags, String introduction, Boolean isActive, String keyword) {
        this.id = id;
        this.name = name;
        this.intraId = intraId;
        this.profileImage = profileImage;
        this.tags = tags;
        this.introduction = introduction;
        this.isActive = isActive;
        this.keyword = keyword;
    }
}
