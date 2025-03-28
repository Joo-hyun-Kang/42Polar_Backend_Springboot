package com._polar._polar_backend_spring.v1.mentoringLogs;

import com._polar._polar_backend_spring.domain.entity.MentoringLogs;
import com._polar._polar_backend_spring.domain.entity.enums.LogStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MentoringLogsRepository {
    @PersistenceContext
    private EntityManager em;

    public List<MentoringLogs> getSimpleLogs(String mentorIntraId, int take, int page) {
        return em.createQuery(
                        "select ml FROM MentoringLogs ml " +
                                "left join fetch ml.reports r " +
                                "join ml.mentors m " +
                                "where m.intraId = :intraId " +
                                "and ml.status = :status " +
                                "order by ml.meetingAt DESC"
                        , MentoringLogs.class
                )
                .setParameter("intraId", mentorIntraId)
                .setParameter("status", LogStatus.DONE)
                .setMaxResults(take)
                .setFirstResult(take * (page - 1))
                .getResultList();
    }

    public List<MentoringLogs> getMentoringsLists(String intraId, int take, int page) {
        return em.createQuery("select ml FROM MentoringLogs ml " +
                                        "left join fetch ml.cadets c " +
                                        "left join fetch ml.reports r " +
                                        "join ml.mentors m " +
                                        "WHERE m.intraId = :intraId " +
                                        "ORDER BY ml.createdAt DESC"
                        , MentoringLogs.class)
                .setParameter("intraId", intraId)
                .setMaxResults(take)
                .setFirstResult(take * (page - 1))
                .getResultList();
    }

    public Long getMentoringLogsCount(String mentorIntraId) {
        return em.createQuery("select count(ml) FROM MentoringLogs ml "
                                + "join ml.mentors m "
                                + "WHERE ml.mentors.intraId = :intraId"
                        , Long.class)
                .setParameter("intraId", mentorIntraId)
                .getSingleResult();
    }
}