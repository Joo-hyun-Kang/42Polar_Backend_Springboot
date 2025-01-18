package com._polar._polar_backend_spring.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity @Getter
@EntityListeners(AuditingEntityListener.class)
public class Bocals {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 10, nullable = true)
    private String name;

    @Column(length = 50, unique = true, nullable = false)
    private String intraId;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    //JPAが使うため、
    public Bocals() {
    }

    public Bocals(String intraId) {
        this.intraId = intraId;
    }

    public void setIntraId(String intraId) {
        this.intraId = intraId;
    }
}
