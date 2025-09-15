package com.pnam.repositories.impl;

import com.pnam.pojo.Course;
import com.pnam.repositories.CourseRepository;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@Transactional
public class CourseRepositoryImpl implements CourseRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Course findById(Long id) {
        Session s = factory.getObject().getCurrentSession();
        return s.find(Course.class, id);
    }

    @Override
    public List<Course> getCourses(Map<String, String> params) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Course> q = b.createQuery(Course.class);
        Root<Course> root = q.from(Course.class);
        q.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            if (params.containsKey("kw")) {
                String kw = "%" + params.get("kw") + "%";
                predicates.add(b.like(root.get("title"), kw));
            }
            if (params.containsKey("cateId")) {
                Long cateId = Long.parseLong(params.get("cateId"));
                predicates.add(b.equal(root.get("categoryId").get("id"), cateId));
            }
            if (params.containsKey("instructorId")) {
                Long instId = Long.parseLong(params.get("instructorId"));
                predicates.add(b.equal(root.get("instructorId").get("id"), instId));
            }
            if (params.containsKey("status")) {
                predicates.add(b.equal(root.get("status"), params.get("status")));
            }
        }

        q.where(predicates.toArray(new Predicate[0]));
        q.orderBy(b.desc(root.get("createdAt"))); // mới nhất lên trước

        TypedQuery<Course> query = s.createQuery(q);
        return query.getResultList();
    }

    @Override
    public Course save(Course c) {
        Session s = factory.getObject().getCurrentSession();
        if (c.getId() == null) {
            s.persist(c);   // thêm mới
            return c;
        } else {
            return s.merge(c); // cập nhật
        }
    }

    @Override
    public void delete(Long id) {
        Session s = factory.getObject().getCurrentSession();
        Course c = s.find(Course.class, id);
        if (c != null) {
            s.remove(c);
        }
    }
}
