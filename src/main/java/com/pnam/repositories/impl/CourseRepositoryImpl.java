package com.pnam.repositories.impl;

import com.pnam.pojo.Course;
import com.pnam.repositories.CourseRepository;
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class CourseRepositoryImpl extends BaseRepository<Course, Long>
        implements CourseRepository {

    @Override
    protected Class<Course> getEntityClass() {
        return Course.class;
    }

    @Override
    public Course getCourseById(Long id) {
        return super.findById(id);
    }

    @Override
    public void saveCourse(Course course) {
        super.save(course, course.getId());
    }

    @Override
    public void deleteCourse(Long id) {
        super.delete(id);
    }

    @Override
    public List<Course> getCourses(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
        Root<Course> root = cq.from(Course.class);
        root.fetch("categoryId", JoinType.LEFT);  
        root.fetch("instructorId", JoinType.LEFT);
        cq.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                predicates.add(cb.or(
                        cb.like(root.get("title"), "%" + kw + "%"),
                        cb.like(root.get("slug"), "%" + kw + "%"),
                        cb.like(root.get("description"), "%" + kw + "%")
                ));
            }

            String status = params.get("status");
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            String cateId = params.get("categoryId");
            if (cateId != null && !cateId.isBlank()) {
                predicates.add(cb.equal(root.get("categoryId").get("id"), Long.parseLong(cateId)));
            }

            String instructorId = params.get("instructorId");
            if (instructorId != null && !instructorId.isBlank()) {
                predicates.add(cb.equal(root.get("instructorId").get("id"), Long.parseLong(instructorId)));
            }

            String minPrice = params.get("minPrice");
            if (minPrice != null && !minPrice.isBlank()) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), Double.parseDouble(minPrice)));
            }

            String maxPrice = params.get("maxPrice");
            if (maxPrice != null && !maxPrice.isBlank()) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), Double.parseDouble(maxPrice)));
            }

            String fromDate = params.get("fromDate");
            if (fromDate != null && !fromDate.isBlank()) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), Date.valueOf(fromDate)));
            }

            String toDate = params.get("toDate");
            if (toDate != null && !toDate.isBlank()) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), Date.valueOf(toDate)));
            }
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        cq.orderBy(cb.desc(root.get("createdAt")));

        Query<Course> query = s.createQuery(cq);

        int page = getPage(params);
        int pageSize = getPageSize(params);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public long countCourses(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Course> root = cq.from(Course.class);
        cq.select(cb.count(root));

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                predicates.add(cb.or(
                        cb.like(root.get("title"), "%" + kw + "%"),
                        cb.like(root.get("slug"), "%" + kw + "%"),
                        cb.like(root.get("description"), "%" + kw + "%")
                ));
            }

            String status = params.get("status");
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            String cateId = params.get("categoryId");
            if (cateId != null && !cateId.isBlank()) {
                predicates.add(cb.equal(root.get("categoryId").get("id"), Long.parseLong(cateId)));
            }

            String instructorId = params.get("instructorId");
            if (instructorId != null && !instructorId.isBlank()) {
                predicates.add(cb.equal(root.get("instructorId").get("id"), Long.parseLong(instructorId)));
            }

            String minPrice = params.get("minPrice");
            if (minPrice != null && !minPrice.isBlank()) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), Double.parseDouble(minPrice)));
            }

            String maxPrice = params.get("maxPrice");
            if (maxPrice != null && !maxPrice.isBlank()) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), Double.parseDouble(maxPrice)));
            }

            String fromDate = params.get("fromDate");
            if (fromDate != null && !fromDate.isBlank()) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), Date.valueOf(fromDate)));
            }

            String toDate = params.get("toDate");
            if (toDate != null && !toDate.isBlank()) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), Date.valueOf(toDate)));
            }
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        return s.createQuery(cq).getSingleResult();
    }

    @Override
    public Course findBySlug(String slug) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
        Root<Course> root = cq.from(Course.class);

        cq.select(root).where(cb.equal(root.get("slug"), slug));

        Query<Course> query = s.createQuery(cq);
        List<Course> results = query.getResultList();

        return results.isEmpty() ? null : results.get(0);
    }

}
