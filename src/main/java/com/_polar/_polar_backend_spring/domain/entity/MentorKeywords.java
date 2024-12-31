package com._polar._polar_backend_spring.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity @Getter
public class MentorKeywords {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keywordsId")
    private Keywords keywords;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentorsId")
    private Mentors mentors;
}
