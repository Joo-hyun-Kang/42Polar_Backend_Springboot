package com._polar._polar_backend_spring.v1.categories.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MentorsListElement {
    private MentorsListInfo mentor;
    private List<String> keywords;
}

