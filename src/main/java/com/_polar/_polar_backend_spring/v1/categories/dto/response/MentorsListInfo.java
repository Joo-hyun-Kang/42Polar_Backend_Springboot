package com._polar._polar_backend_spring.v1.categories.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MentorsListInfo {
    private String id;
    private String name;
    private String intraId;
    private String tags;
    private String profileImage;
    private String introduction;
    @JsonProperty("isActive")
    private boolean isActive;
}
