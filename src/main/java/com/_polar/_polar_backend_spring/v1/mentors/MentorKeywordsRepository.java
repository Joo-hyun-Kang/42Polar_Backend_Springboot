package com._polar._polar_backend_spring.v1.mentors;

import com._polar._polar_backend_spring.domain.entity.MentorKeywords;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MentorKeywordsRepository {
    @PersistenceContext
    private EntityManager em;

    public List<MentorKeywords> findByIntraOrNull(String intraId) {
        String jpql = "select mk from MentorKeywords mk " +
                        "join fetch mk.keywords k " +
                        "join fetch mk.mentors m " +
                        "where m.intraId = :intraId";
        TypedQuery<MentorKeywords> query = em.createQuery(jpql, MentorKeywords.class);
        query.setParameter("intraId", intraId);

        return query.getResultList();
    }
}
