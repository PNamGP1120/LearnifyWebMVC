/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pnam.repositories.impl;

import com.pnam.pojo.Lesson;
import com.pnam.repositories.LessonRepository;
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
public class LessonRepositoryImpl implements LessonRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Lesson findById(Long id) {
        return factory.getObject().getCurrentSession().find(Lesson.class, id);
    }

    @Override
    public List<Lesson> findBySection(Long sectionId) {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<Lesson> q = s.createQuery(
            "SELECT l FROM Lesson l WHERE l.sectionId.id = :sid", Lesson.class);
        q.setParameter("sid", sectionId);
        return q.getResultList();
    }

    @Override
    public Lesson save(Lesson l) {
        Session s = factory.getObject().getCurrentSession();
        if (l.getId() == null) {
            s.persist(l);
            return l;
        } else {
            return s.merge(l);
        }
    }

    @Override
    public void delete(Long id) {
        Session s = factory.getObject().getCurrentSession();
        Lesson l = s.find(Lesson.class, id);
        if (l != null) s.remove(l);
    }
}

