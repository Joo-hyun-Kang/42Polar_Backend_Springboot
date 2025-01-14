package com._polar._polar_backend_spring.v1.cadets;

import com._polar._polar_backend_spring.domain.entity.Cadets;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CadetsRepository {

    @PersistenceContext
    private EntityManager em;

    public Cadets findByIntraOrNull(String intraId) {
        try {
            String jpql = "SELECT c FROM Cadets c WHERE c.intraId = :intraId";
            TypedQuery<Cadets> query = em.createQuery(jpql, Cadets.class);
            query.setParameter("intraId", intraId);

            return query.getSingleResult();
        } catch (Exception e) {
            //• 結果がなければ: javax.persistence.NoResultException
            //• 結果が二つ以上なら: javax.persistence.NonUniqueResultException
            log.error(e.toString());
            return null;
        }
    }

    public void save(Cadets cadet) {
        em.persist(cadet);
    }
}