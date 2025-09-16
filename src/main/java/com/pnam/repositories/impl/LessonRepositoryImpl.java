package com.pnam.repositories.impl;

import com.pnam.pojo.Lesson;
import com.pnam.repositories.LessonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class LessonRepositoryImpl implements LessonRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Lesson> getLessons(Map<String, String> params) {
        String jpql = "SELECT l FROM Lesson l WHERE 1=1";

        String kw = params.get("kw");
        if (kw != null && !kw.isEmpty()) {
            jpql += " AND (l.title LIKE :kw OR l.contentUrl LIKE :kw)";
        }

        TypedQuery<Lesson> q = em.createQuery(jpql, Lesson.class);
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
    public long countLessons(Map<String, String> params) {
        String jpql = "SELECT COUNT(l) FROM Lesson l WHERE 1=1";
        String kw = params.get("kw");

        if (kw != null && !kw.isEmpty()) {
            jpql += " AND (l.title LIKE :kw OR l.contentUrl LIKE :kw)";
        }

        TypedQuery<Long> q = em.createQuery(jpql, Long.class);
        if (kw != null && !kw.isEmpty()) {
            q.setParameter("kw", "%" + kw + "%");
        }

        return q.getSingleResult();
    }

    @Override
    public Lesson findById(Long id) {
        return em.find(Lesson.class, id);
    }

    @Override
    public void save(Lesson lesson) {
        if (lesson.getId() == null) {
            em.persist(lesson);
        } else {
            em.merge(lesson);
        }
    }

    @Override
    public void delete(Long id) {
        Lesson l = findById(id);
        if (l != null) {
            em.remove(l);
        }
    }
}
