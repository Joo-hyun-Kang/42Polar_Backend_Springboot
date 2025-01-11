package com._polar._polar_backend_spring.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity @Getter @Setter
@EntityListeners(AuditingEntityListener.class)
public class Cadets {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 15, unique = true)
    private String intraId;

    @Column(length = 50, nullable = true)
    private String name;

    @Column(length = 1000, nullable = true)
    private String profileImage;

    @Column(length = 1000, nullable = true)
    private String resumeUrl;

    @Column(nullable = false)
    private boolean isCommon;

    @Column(length = 100, nullable = false)
    private String email;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @OneToMany(mappedBy = "cadets")
    private List<Comments> comments;

    @OneToMany(mappedBy = "cadets")
    private List<MentoringLogs> mentoringLogs;

    @OneToMany(mappedBy = "cadets")
    private List<Reports> reports;

    //JPAが使うため、
    public Cadets() {
    }

    public Cadets(String intraId, String profileImage, boolean isCommon, String email) {
        this.intraId = intraId;
        this.profileImage = profileImage;
        this.isCommon = isCommon;
        this.email = email;
    }

    public boolean isInitialized() {
        return this.name != null;
    }
}
