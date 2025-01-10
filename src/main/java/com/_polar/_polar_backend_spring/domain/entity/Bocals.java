package com._polar._polar_backend_spring.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import java.util.Date;
import java.util.UUID;

@Entity @Getter
public class Bocals {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 10, nullable = true)
    private String name;

    @Column(length = 50, unique = true, nullable = false)
    private String intraId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public Bocals(String intraId) {
        this.intraId = intraId;
    }

    public void setIntraId(String intraId) {
        this.intraId = intraId;
    }
}
