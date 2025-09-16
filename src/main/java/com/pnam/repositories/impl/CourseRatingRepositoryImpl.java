package com.pnam.repositories.impl;

import com.pnam.pojo.CourseRating;
import com.pnam.repositories.CourseRatingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class CourseRatingRepositoryImpl implements CourseRatingRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<CourseRating> getRatings(Map<String, String> params) {
        String jpql = "SELECT r FROM CourseRating r WHERE 1=1";

        String kw = params.get("kw");
        if (kw != null && !kw.isEmpty()) {
            jpql += " AND r.comment LIKE :kw";
        }

        TypedQuery<CourseRating> q = em.createQuery(jpql, CourseRating.class);
        if (kw != null && !kw.isEmpty()) {
            q.setParameter("kw", "%" + kw + "%");
        }

        int page = params.get("page") != null ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize")) : 10;

        q.setFirstResult((page - 1) * pageSize);
        q.setMaxResults(pageSize);

        return q.getResultList();
    }

    @Override
    public long countRatings(Map<String, String> params) {
        String jpql = "SELECT COUNT(r) FROM CourseRating r WHERE 1=1";
        String kw = params.get("kw");

        if (kw != null && !kw.isEmpty()) {
            jpql += " AND r.comment LIKE :kw";
        }

        TypedQuery<Long> q = em.createQuery(jpql, Long.class);
        if (kw != null && !kw.isEmpty()) {
            q.setParameter("kw", "%" + kw + "%");
        }

        return q.getSingleResult();
    }

    @Override
    public CourseRating findById(Long id) {
        return em.find(CourseRating.class, id);
    }

    @Override
    public void save(CourseRating rating) {
        if (rating.getId() == null) {
            em.persist(rating);
        } else {
            em.merge(rating);
        }
    }

    @Override
    public void delete(Long id) {
        CourseRating r = findById(id);
        if (r != null) {
            em.remove(r);
        }
    }
}
