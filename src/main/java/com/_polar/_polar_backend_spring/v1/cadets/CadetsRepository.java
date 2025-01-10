package com._polar._polar_backend_spring.v1.cadets;

import com._polar._polar_backend_spring.domain.entity.Cadets;
import com._polar._polar_backend_spring.domain.entity.Mentors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class CadetsRepository {

    @PersistenceContext
    private EntityManager em;

    public Cadets findByIntra(String intraId) {
        return em.find(Cadets.class, intraId);
    }

    public void save(Cadets cadet) {
        em.persist(cadet);
    }
}