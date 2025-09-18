package com.pnam.repositories.impl;

import com.pnam.pojo.Enrollment;
import com.pnam.repositories.EnrollmentRepository;
import com.pnam.repositories.base.BaseRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class EnrollmentRepositoryImpl extends BaseRepository<Enrollment, Long>
        implements EnrollmentRepository {

    @Override
    protected Class<Enrollment> getEntityClass() {
        return Enrollment.class;
    }

    @Override
    public List<Enrollment> getEnrollments(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Enrollment> cq = cb.createQuery(Enrollment.class);
        Root<Enrollment> root = cq.from(Enrollment.class);
        var courseFetch = root.fetch("courseId", JoinType.LEFT);
        courseFetch.fetch("categoryId", JoinType.LEFT);
        courseFetch.fetch("instructorId", JoinType.LEFT);

        root.fetch("studentId", JoinType.LEFT);

        cq.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String studentId = params.get("studentId");
            if (studentId != null && !studentId.isBlank()) {
                predicates.add(cb.equal(root.get("studentId").get("id"), Long.parseLong(studentId)));
            }

            String courseId = params.get("courseId");
            if (courseId != null && !courseId.isBlank()) {
                predicates.add(cb.equal(root.get("courseId").get("id"), Long.parseLong(courseId)));
            }

            String status = params.get("accessStatus");
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("accessStatus"), status));
            }
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        cq.orderBy(cb.desc(root.get("enrolledAt")));

        Query<Enrollment> query = s.createQuery(cq);

        int page = getPage(params);
        int pageSize = getPageSize(params);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public long countEnrollments(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Enrollment> root = cq.from(Enrollment.class);
        cq.select(cb.count(root));

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String status = params.get("accessStatus");
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("accessStatus"), status));
            }
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        return s.createQuery(cq).getSingleResult();
    }

    @Override
    public Enrollment findById(Long id) {
        return super.findById(id);
    }

    @Override
    public void save(Enrollment e) {
        super.save(e, e.getId());
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }
}
