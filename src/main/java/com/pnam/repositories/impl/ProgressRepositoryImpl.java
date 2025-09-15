/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pnam.repositories.impl;

import com.pnam.pojo.Progress;
import com.pnam.repositories.ProgressRepository;
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
public class ProgressRepositoryImpl implements ProgressRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Progress findById(Long id) {
        return factory.getObject().getCurrentSession().find(Progress.class, id);
    }

    @Override
    public List<Progress> findByEnrollment(Long enrollmentId) {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<Progress> q = s.createQuery(
            "SELECT p FROM Progress p WHERE p.enrollmentId.id = :eid", Progress.class);
        q.setParameter("eid", enrollmentId);
        return q.getResultList();
    }

    @Override
    public Progress save(Progress p) {
        Session s = factory.getObject().getCurrentSession();
        if (p.getId() == null) {
            s.persist(p);
            return p;
        } else {
            return s.merge(p);
        }
    }

    @Override
    public void delete(Long id) {
        Session s = factory.getObject().getCurrentSession();
        Progress p = s.find(Progress.class, id);
        if (p != null) s.remove(p);
    }
}
