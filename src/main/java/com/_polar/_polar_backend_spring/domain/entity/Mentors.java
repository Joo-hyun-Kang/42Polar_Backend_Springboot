package com._polar._polar_backend_spring.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity @Getter
public class Mentors {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 50, unique = true, nullable = false)
    private String intraId;

    @Column(length = 100, nullable = true)
    private String slackId;

    @Column(length = 50, nullable = true)
    private String name;

    @Column(length = 100, nullable = true)
    private String email;

    @Column(length = 100, nullable = true)
    private String company;

    @Column(length = 100, nullable = true)
    private String duty;

    @Column(length = 100, nullable = true)
    private String profileImage;

    @Column(nullable = true)
    private String availableTime;

    @Column(length = 150, nullable = true)
    private String introduction;

    // PostgreSQL ARRAY 型を利用
    // JPA的には＠ElementCollectionを利用して、別のテーブルを作ること
    @Column(columnDefinition = "TEXT[]")
    private List<String> tags = new ArrayList<>();

    private boolean isActive;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String markdownContent;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    @OneToMany(mappedBy = "mentors")
    private List<MentorKeywords> mentorKeywords = new ArrayList<>();

    @OneToMany(mappedBy = "mentors")
    private List<Comments> comments = new ArrayList<>();

    @OneToMany(mappedBy = "mentors")
    private List<MentoringLogs> mentoringLogs = new ArrayList<>();

    @OneToMany(mappedBy = "mentors")
    private List<Reports> reports = new ArrayList<>();
}
