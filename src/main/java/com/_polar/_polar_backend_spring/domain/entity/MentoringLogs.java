package com._polar._polar_backend_spring.domain.entity;

import com._polar._polar_backend_spring.domain.entity.Cadets;
import com._polar._polar_backend_spring.domain.entity.Mentors;
import com._polar._polar_backend_spring.domain.entity.Reports;
import com._polar._polar_backend_spring.domain.entity.enums.LogStatus;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity @Getter @EntityListeners(AuditingEntityListener.class)
public class MentoringLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(columnDefinition = "TIMESTAMP[]", nullable = true)
    private List<Date> meetingAt = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date meetingStart;

    @Column(length = 100, nullable = false)
    private String topic;

    @Column(length = 1000, nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private LogStatus status = LogStatus.WATING;

    @Column(length = 500, nullable = true)
    private String rejectMessage;

    @Column(columnDefinition = "TIMESTAMP[]")
    private List<Date> requestTime1;

    @Column(columnDefinition = "TIMESTAMP[]", nullable = true)
    private List<Date> requestTime2;

    @Column(columnDefinition = "TIMESTAMP[]", nullable = true)
    private List<Date> requestTime3;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentorId")
    private Mentors mentors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cadetId")
    private Cadets cadets;

    @OneToOne(mappedBy = "mentoringLogs", fetch = FetchType.LAZY)
    private Reports reports;
}
