package com._polar._polar_backend_spring.domain.entity;

import com._polar._polar_backend_spring.domain.convertor.LogStatusConverter;
import com._polar._polar_backend_spring.domain.entity.enums.LogStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
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

    @Convert(converter = LogStatusConverter.class)
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
    @JoinColumn(name = "mentorsId")
    private Mentors mentors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cadetsId")
    private Cadets cadets;

    /*
       話題:OneToOne自動的にクエリが出る「@OneToMany(mappedBy = "mentoringLogs", fetch = FetchType.LAZY)」
        ーOneToOneは対象テーブル(Reportにキーがあるので）この場合
        ーfetch = FetchType.LAZY貼り付けば、自動的に追加クエリが出る
        ーそれとも、貼り付なけば、基本的にfetch Joinで持ってくる

       解決策：ManyToOne OneToManyに、それとも、主テーブルにキー（これはキーにヌルが来れる短所）
        ー@OneToMany(mappedBy = "mentoringLogs", fetch = FetchType.LAZY)
        private List<Reports> reports;
        ー@ManyToOne(fetch = FetchType.LAZY)
          @JoinColumn(name = "mentoringLogsId")
          private MentoringLogs mentoringLogs;
    */
    @OneToOne(mappedBy = "mentoringLogs")
    private Reports reports;
}
