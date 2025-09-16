package com.pnam.repositories.impl;

import com.pnam.pojo.Enrollment;
import com.pnam.repositories.EnrollmentRepository;
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
public class EnrollmentRepositoryImpl implements EnrollmentRepository {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public List<Enrollment> getEnrollments(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Enrollment> q = b.createQuery(Enrollment.class);
        Root<Enrollment> root = q.from(Enrollment.class);
        q.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String studentId = params.get("studentId");
            if (studentId != null && !studentId.isBlank()) {
                predicates.add(b.equal(root.get("studentId").get("id"), Long.parseLong(studentId)));
            }

            String courseId = params.get("courseId");
            if (courseId != null && !courseId.isBlank()) {
                predicates.add(b.equal(root.get("courseId").get("id"), Long.parseLong(courseId)));
            }

            String status = params.get("accessStatus");
            if (status != null && !status.isBlank()) {
                predicates.add(b.equal(root.get("accessStatus"), status));
            }
        }

        q.where(predicates.toArray(Predicate[]::new));
        q.orderBy(b.desc(root.get("enrolledAt")));

        Query query = s.createQuery(q);
        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.get("page"));
            query.setFirstResult((page - 1) * PAGE_SIZE);
            query.setMaxResults(PAGE_SIZE);
        }

        return query.getResultList();
    }

    @Override
    public long countEnrollments(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<Enrollment> root = q.from(Enrollment.class);
        q.select(b.count(root));

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String status = params.get("accessStatus");
            if (status != null && !status.isBlank()) {
                predicates.add(b.equal(root.get("accessStatus"), status));
            }
        }
        q.where(predicates.toArray(Predicate[]::new));

        return s.createQuery(q).getSingleResult();
    }

    @Override
    public Enrollment findById(Long id) {
        return getSession().get(Enrollment.class, id);
    }

    @Override
    public void save(Enrollment e) {
        Session s = getSession();
        if (e.getId() == null) {
            s.persist(e);
        } else {
            s.merge(e);
        }
    }

    @Override
    public void delete(Long id) {
        Enrollment e = findById(id);
        if (e != null) {
            getSession().remove(e);
        }
    }
}
