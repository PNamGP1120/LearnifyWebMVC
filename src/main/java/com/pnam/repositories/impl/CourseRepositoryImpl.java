package com.pnam.repositories.impl;

import com.pnam.pojo.Course;
import com.pnam.repositories.CourseRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class CourseRepositoryImpl implements CourseRepository {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getSession() {
        return this.factory.getObject().getCurrentSession();
    }

    @Override
    public List<Course> getCourses(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Course> q = b.createQuery(Course.class);
        Root<Course> root = q.from(Course.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                Predicate titleLike = b.like(root.get("title"), "%" + kw + "%");
                Predicate slugLike = b.like(root.get("slug"), "%" + kw + "%");
                Predicate descLike = b.like(root.get("description"), "%" + kw + "%");
                predicates.add(b.or(titleLike, slugLike, descLike));
            }

            String status = params.get("status");
            if (status != null && !status.isEmpty()) {
                predicates.add(b.equal(root.get("status"), status));
            }

            String cateId = params.get("categoryId");
            if (cateId != null && !cateId.isEmpty()) {
                predicates.add(b.equal(root.get("categoryId").get("id"), Long.parseLong(cateId)));
            }

            String instructorId = params.get("instructorId");
            if (instructorId != null && !instructorId.isEmpty()) {
                predicates.add(b.equal(root.get("instructorId").get("id"), Long.parseLong(instructorId)));
            }

            String minPrice = params.get("minPrice");
            if (minPrice != null && !minPrice.isEmpty()) {
                predicates.add(b.greaterThanOrEqualTo(root.get("price"), Double.parseDouble(minPrice)));
            }

            String maxPrice = params.get("maxPrice");
            if (maxPrice != null && !maxPrice.isEmpty()) {
                predicates.add(b.lessThanOrEqualTo(root.get("price"), Double.parseDouble(maxPrice)));
            }

            String fromDate = params.get("fromDate");
            if (fromDate != null && !fromDate.isEmpty()) {
                predicates.add(b.greaterThanOrEqualTo(root.get("createdAt"), Date.valueOf(fromDate)));
            }

            String toDate = params.get("toDate");
            if (toDate != null && !toDate.isEmpty()) {
                predicates.add(b.lessThanOrEqualTo(root.get("createdAt"), Date.valueOf(toDate)));
            }

            q.where(predicates.toArray(Predicate[]::new));

            // Sắp xếp theo ngày tạo mới nhất
            q.orderBy(b.desc(root.get("createdAt")));
        }

        Query query = s.createQuery(q);

        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.get("page"));
            int start = (page - 1) * PAGE_SIZE;

            query.setFirstResult(start);
            query.setMaxResults(PAGE_SIZE);
        }

        return query.getResultList();
    }

    @Override
    public long countCourses(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<Course> root = q.from(Course.class);
        q.select(b.count(root));

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                Predicate titleLike = b.like(root.get("title"), "%" + kw + "%");
                Predicate slugLike = b.like(root.get("slug"), "%" + kw + "%");
                Predicate descLike = b.like(root.get("description"), "%" + kw + "%");
                predicates.add(b.or(titleLike, slugLike, descLike));
            }

            String status = params.get("status");
            if (status != null && !status.isEmpty()) {
                predicates.add(b.equal(root.get("status"), status));
            }

            String cateId = params.get("categoryId");
            if (cateId != null && !cateId.isEmpty()) {
                predicates.add(b.equal(root.get("categoryId").get("id"), Long.parseLong(cateId)));
            }

            String instructorId = params.get("instructorId");
            if (instructorId != null && !instructorId.isEmpty()) {
                predicates.add(b.equal(root.get("instructorId").get("id"), Long.parseLong(instructorId)));
            }

            String minPrice = params.get("minPrice");
            if (minPrice != null && !minPrice.isEmpty()) {
                predicates.add(b.greaterThanOrEqualTo(root.get("price"), Double.parseDouble(minPrice)));
            }

            String maxPrice = params.get("maxPrice");
            if (maxPrice != null && !maxPrice.isEmpty()) {
                predicates.add(b.lessThanOrEqualTo(root.get("price"), Double.parseDouble(maxPrice)));
            }

            String fromDate = params.get("fromDate");
            if (fromDate != null && !fromDate.isEmpty()) {
                predicates.add(b.greaterThanOrEqualTo(root.get("createdAt"), Date.valueOf(fromDate)));
            }

            String toDate = params.get("toDate");
            if (toDate != null && !toDate.isEmpty()) {
                predicates.add(b.lessThanOrEqualTo(root.get("createdAt"), Date.valueOf(toDate)));
            }

            q.where(predicates.toArray(Predicate[]::new));
        }

        return s.createQuery(q).getSingleResult();
    }

    @Override
    public Course findById(Long id) {
        return getSession().get(Course.class, id);
    }

    @Override
    public void save(Course course) {
        Session s = getSession();
        if (course.getId() == null) {
            s.persist(course);
        } else {
            s.merge(course);
        }
    }

    @Override
    public void delete(Long id) {
        Session s = getSession();
        Course c = findById(id);
        if (c != null) {
            s.remove(c);
        }
    }
}
