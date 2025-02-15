package com._polar._polar_backend_spring.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity @Getter
public class Keywords {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 50, unique = true)
    private String name;

    @OneToMany(mappedBy = "keywords")
    private final List<KeywordCategories> keywordCategories = new ArrayList<>();

    @OneToMany(mappedBy = "keywords")
    private final List<MentorKeywords> mentorKeywords = new ArrayList<>();
}
