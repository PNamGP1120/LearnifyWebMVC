/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pnam.repositories.impl;

import com.pnam.pojo.Payment;
import com.pnam.repositories.PaymentRepository;
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
public class PaymentRepositoryImpl implements PaymentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Payment findById(Long id) {
        return factory.getObject().getCurrentSession().find(Payment.class, id);
    }

    @Override
    public List<Payment> findByEnrollment(Long enrollmentId) {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<Payment> q = s.createQuery(
                "SELECT p FROM Payment p WHERE p.enrollmentId.id = :eid", Payment.class);
        q.setParameter("eid", enrollmentId);
        return q.getResultList();
    }

    @Override
    public Payment save(Payment p) {
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
        Payment p = s.find(Payment.class, id);
        if (p != null) {
            s.remove(p);
        }
    }

    @Override
    public List<Payment> findAll() {
        Session s = factory.getObject().getCurrentSession();
        return s.createQuery("FROM Payment p ORDER BY p.createdAt DESC", Payment.class)
                .getResultList();
    }
}
