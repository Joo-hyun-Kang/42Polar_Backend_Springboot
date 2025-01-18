package com._polar._polar_backend_spring.domain.entity;

import com._polar._polar_backend_spring.domain.convertor.ReportStatusConverter;
import com._polar._polar_backend_spring.domain.entity.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Reports {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 500, nullable = true)
    private String extraCadets;

    @Column(length = 100, nullable = true)
    private String place;

    @Column(length = 150, nullable = true)
    private String topic;

    @Column(length = 800, nullable = true)
    private String content;

    @Column(columnDefinition = "TEXT[]", length = 1000, nullable = true)
    private List<String> imageUrl;

    @Column(length = 1000, nullable = true)
    private String signatureUrl;

    @Column(length = 800, nullable = true)
    private String feedbackMessage;

    @Column(nullable = true)
    private Integer feedback1;

    @Column(nullable = true)
    private Integer feedback2;

    @Column(nullable = true)
    private Integer feedback3;

    @Column(nullable = true)
    private Integer money;

    @Convert(converter = ReportStatusConverter.class)
    private ReportStatus status;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    //fetch = FetchType.LAZY貼り付けなければ、自動的にfetch Joinで持ってくる
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentoringLogsId")
    private MentoringLogs mentoringLogs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentorsId")
    private Mentors mentors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cadetsId")
    private Cadets cadets;
}
