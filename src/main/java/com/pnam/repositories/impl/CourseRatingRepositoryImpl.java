/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pnam.repositories.impl;

import com.pnam.pojo.CourseRating;
import com.pnam.repositories.CourseRatingRepository;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author pnam
 */
@Repository
@Transactional
public class CourseRatingRepositoryImpl implements CourseRatingRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public CourseRating findById(Long id) {
        return factory.getObject().getCurrentSession().find(CourseRating.class, id);
    }

    @Override
    public List<CourseRating> findByCourse(Long courseId) {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<CourseRating> q = s.createQuery(
            "SELECT cr FROM CourseRating cr WHERE cr.courseId.id = :cid", CourseRating.class);
        q.setParameter("cid", courseId);
        return q.getResultList();
    }

    @Override
    public List<CourseRating> findByStudent(Long studentId) {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<CourseRating> q = s.createQuery(
            "SELECT cr FROM CourseRating cr WHERE cr.studentId.id = :sid", CourseRating.class);
        q.setParameter("sid", studentId);
        return q.getResultList();
    }

    @Override
    public CourseRating findByCourseAndStudent(Long courseId, Long studentId) {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<CourseRating> q = s.createQuery(
            "SELECT cr FROM CourseRating cr WHERE cr.courseId.id = :cid AND cr.studentId.id = :sid",
            CourseRating.class);
        q.setParameter("cid", courseId);
        q.setParameter("sid", studentId);
        return q.getResultStream().findFirst().orElse(null);
    }

    @Override
    public CourseRating save(CourseRating cr) {
        Session s = factory.getObject().getCurrentSession();
        if (cr.getId() == null) {
            s.persist(cr);
            return cr;
        } else {
            return s.merge(cr);
        }
    }

    @Override
    public void delete(Long id) {
        Session s = factory.getObject().getCurrentSession();
        CourseRating cr = s.find(CourseRating.class, id);
        if (cr != null) s.remove(cr);
    }
}

