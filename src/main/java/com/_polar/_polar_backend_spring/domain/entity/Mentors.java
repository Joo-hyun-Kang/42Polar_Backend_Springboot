package com._polar._polar_backend_spring.domain.entity;

import com._polar._polar_backend_spring.domain.dto.AvailableTimeDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity @Getter @Setter
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "mentors")
    private List<MentorKeywords> mentorKeywords = new ArrayList<>();

    @OneToMany(mappedBy = "mentors")
    private List<Comments> comments = new ArrayList<>();

    @OneToMany(mappedBy = "mentors")
    private List<MentoringLogs> mentoringLogs = new ArrayList<>();

    @OneToMany(mappedBy = "mentors")
    private List<Reports> reports = new ArrayList<>();

    //JPAが使うため、
    public Mentors() {
    }

    public Mentors(String intraId) {
        this.intraId = intraId;
        this.isActive = false;
    }

    public boolean isInitialized() {
        if (slackId == null || email == null || name == null ||
                duty == null || company == null) {
            return false;
        }

        if (isActive && availableTime == null) {
            return false;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<List<AvailableTimeDto>> week = objectMapper.readValue(availableTime, new TypeReference<>() {});

            return week.stream().anyMatch(day -> !day.isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
