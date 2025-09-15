/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pnam.repositories.impl;

import com.pnam.pojo.CourseSection;
import com.pnam.repositories.CourseSectionRepository;
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
public class CourseSectionRepositoryImpl implements CourseSectionRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public CourseSection findById(Long id) {
        return factory.getObject().getCurrentSession().find(CourseSection.class, id);
    }

    @Override
    public List<CourseSection> findByCourse(Long courseId) {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<CourseSection> q = s.createQuery(
            "SELECT cs FROM CourseSection cs WHERE cs.courseId.id = :cid", CourseSection.class);
        q.setParameter("cid", courseId);
        return q.getResultList();
    }

    @Override
    public CourseSection save(CourseSection cs) {
        Session s = factory.getObject().getCurrentSession();
        if (cs.getId() == null) {
            s.persist(cs);
            return cs;
        } else {
            return s.merge(cs);
        }
    }

    @Override
    public void delete(Long id) {
        Session s = factory.getObject().getCurrentSession();
        CourseSection cs = s.find(CourseSection.class, id);
        if (cs != null) s.remove(cs);
    }
}
