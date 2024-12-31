package com._polar._polar_backend_spring.v1.categories;

import com._polar._polar_backend_spring.domain.entity.Categories;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoriesRepository {
    @PersistenceContext
    private EntityManager em;

    public List<Categories> findAll() {
        final int TAKE_COUNT = 8;

        return em.createQuery("select c from Categories c", Categories.class)
//                .setMaxResults(TAKE_COUNT)
                .getResultList();
    }
}
