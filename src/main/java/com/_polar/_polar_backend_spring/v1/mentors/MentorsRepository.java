package com._polar._polar_backend_spring.v1.mentors;

import com._polar._polar_backend_spring.domain.entity.Mentors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MentorsRepository {

    @PersistenceContext
    private EntityManager em;

    public Mentors findByIntraOrNull(String intraId) {
        try {
            String jpql = "SELECT m FROM Mentors m WHERE m.intraId = :intraId";
            TypedQuery<Mentors> query = em.createQuery(jpql, Mentors.class);
            query.setParameter("intraId", intraId);

            return query.getSingleResult();
        } catch (Exception e) {
            //• 結果がなければ: javax.persistence.NoResultException
            //• 結果が二つ以上なら: javax.persistence.NonUniqueResultException
            log.error(e.toString());
            return null;
        }
    }

    public void save(Mentors mentors) {
        em.persist(mentors);
    }
}
