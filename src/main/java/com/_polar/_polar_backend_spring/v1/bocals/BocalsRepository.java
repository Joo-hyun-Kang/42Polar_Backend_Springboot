package com._polar._polar_backend_spring.v1.bocals;

import com._polar._polar_backend_spring.domain.entity.Bocals;
import com._polar._polar_backend_spring.domain.entity.Mentors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class BocalsRepository {

    @PersistenceContext
    private EntityManager em;

    public Bocals findByIntra(String intraId) {
        return em.find(Bocals.class, intraId);
    }

    public void save(Bocals bocal) {
        em.persist(bocal);
    }
}