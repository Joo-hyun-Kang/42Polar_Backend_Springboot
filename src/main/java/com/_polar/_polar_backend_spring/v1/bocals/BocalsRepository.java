package com._polar._polar_backend_spring.v1.bocals;

import com._polar._polar_backend_spring.domain.entity.Bocals;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class BocalsRepository {

    @PersistenceContext
    private EntityManager em;

    public Bocals findByIntraOrNull(String intraId) {
        try {
            String jpql = "SELECT b FROM Bocals b WHERE b.intraId = :intraId";
            TypedQuery<Bocals> query = em.createQuery(jpql, Bocals.class);
            query.setParameter("intraId", intraId);

            return query.getSingleResult();
        } catch (Exception e) {
            //• 結果がなければ: javax.persistence.NoResultException
            //• 結果が二つ以上なら: javax.persistence.NonUniqueResultException
            log.error(e.toString());
            return null;
        }
    }

    public void save(Bocals bocal) {
        em.persist(bocal);
    }
}