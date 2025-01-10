package com._polar._polar_backend_spring.v1.mentors;

import com._polar._polar_backend_spring.domain.entity.Mentors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MentorsRepository {

    @PersistenceContext
    private EntityManager em;

    public Mentors findByIntra(String intraId) {
        return em.find(Mentors.class, intraId);
    }

    public void save(Mentors mentors) {
        em.persist(mentors);
    }
}
