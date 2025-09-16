package com.pnam.repositories.impl;

import com.pnam.pojo.CourseSection;
import com.pnam.repositories.CourseSectionRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class CourseSectionRepositoryImpl implements CourseSectionRepository {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getSession() {
        return this.factory.getObject().getCurrentSession();
    }

    @Override
    public List<CourseSection> getSections(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<CourseSection> q = b.createQuery(CourseSection.class);
        Root<CourseSection> root = q.from(CourseSection.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            // lọc theo keyword
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("title"), "%" + kw + "%"));
            }

            // lọc theo courseId
            String courseId = params.get("courseId");
            if (courseId != null && !courseId.isEmpty()) {
                predicates.add(b.equal(root.get("courseId").get("id"), Long.parseLong(courseId)));
            }

            q.where(predicates.toArray(Predicate[]::new));
            q.orderBy(b.asc(root.get("orderIndex"))); // sắp xếp theo thứ tự section
        }

        Query query = s.createQuery(q);

        // phân trang
        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.get("page"));
            int start = (page - 1) * PAGE_SIZE;
            query.setFirstResult(start);
            query.setMaxResults(PAGE_SIZE);
        }

        return query.getResultList();
    }

    @Override
    public long countSections(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<CourseSection> root = q.from(CourseSection.class);
        q.select(b.count(root));

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("title"), "%" + kw + "%"));
            }

            String courseId = params.get("courseId");
            if (courseId != null && !courseId.isEmpty()) {
                predicates.add(b.equal(root.get("courseId").get("id"), Long.parseLong(courseId)));
            }

            q.where(predicates.toArray(Predicate[]::new));
        }

        return s.createQuery(q).getSingleResult();
    }

    @Override
    public CourseSection findById(Long id) {
        return getSession().get(CourseSection.class, id);
    }

    @Override
    public void save(CourseSection section) {
        Session s = getSession();
        if (section.getId() == null) {
            s.persist(section);
        } else {
            s.merge(section);
        }
    }

    @Override
    public void delete(Long id) {
        Session s = getSession();
        CourseSection section = findById(id);
        if (section != null) {
            s.remove(section);
        }
    }
}
