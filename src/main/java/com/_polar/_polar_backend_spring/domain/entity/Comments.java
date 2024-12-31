package com._polar._polar_backend_spring.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity @Getter
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 300)
    private String content;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentorsId")
    private Mentors mentors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cadetId")
    private Cadets cadets;
}
