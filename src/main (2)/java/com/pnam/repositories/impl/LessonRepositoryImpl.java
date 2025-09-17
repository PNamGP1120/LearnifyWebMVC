package com.pnam.repositories.impl;

import com.pnam.pojo.Lesson;
import com.pnam.repositories.LessonRepository;
import com.pnam.repositories.base.BaseRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class LessonRepositoryImpl extends BaseRepository<Lesson, Long>
        implements LessonRepository {

    @Override
    protected Class<Lesson> getEntityClass() {
        return Lesson.class;
    }

    @Override
    public List<Lesson> getLessons(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Lesson> q = b.createQuery(Lesson.class);
        Root<Lesson> root = q.from(Lesson.class);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            if (params.containsKey("sectionId")) {
                predicates.add(
                        b.equal(root.get("sectionId").get("id"), Long.parseLong(params.get("sectionId")))
                );
            }
            if (params.containsKey("kw")) {
                predicates.add(
                        b.like(root.get("title"), "%" + params.get("kw") + "%")
                );
            }
        }

        q.select(root).where(predicates.toArray(new Predicate[0]));
        q.orderBy(b.asc(root.get("orderIndex")));

        Query query = s.createQuery(q);

        if (params != null && params.containsKey("page") && params.containsKey("pageSize")) {
            int page = Integer.parseInt(params.get("page"));
            int pageSize = Integer.parseInt(params.get("pageSize"));
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
        }

        return query.getResultList();
    }

    @Override
    public long countLessons(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Lesson> root = cq.from(Lesson.class);
        cq.select(cb.count(root));

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                predicates.add(cb.or(
                        cb.like(root.get("title"), "%" + kw + "%"),
                        cb.like(root.get("contentUrl"), "%" + kw + "%")
                ));
            }
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        return s.createQuery(cq).getSingleResult();
    }

    @Override
    public Lesson findById(Long id) {
        return super.findById(id);
    }

    @Override
    public void save(Lesson lesson) {
        super.save(lesson, lesson.getId());
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }
}
