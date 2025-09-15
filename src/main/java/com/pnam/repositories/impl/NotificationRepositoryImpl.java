/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pnam.repositories.impl;

import com.pnam.pojo.Notification;
import com.pnam.repositories.NotificationRepository;
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
public class NotificationRepositoryImpl implements NotificationRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Notification findById(Long id) {
        return factory.getObject().getCurrentSession().find(Notification.class, id);
    }

    @Override
    public List<Notification> findByUser(Long userId) {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<Notification> q = s.createQuery(
            "SELECT n FROM Notification n WHERE n.userId.id = :uid ORDER BY n.createdAt DESC",
            Notification.class);
        q.setParameter("uid", userId);
        return q.getResultList();
    }

    @Override
    public Notification save(Notification n) {
        Session s = factory.getObject().getCurrentSession();
        if (n.getId() == null) {
            s.persist(n);
            return n;
        } else {
            return s.merge(n);
        }
    }

    @Override
    public void delete(Long id) {
        Session s = factory.getObject().getCurrentSession();
        Notification n = s.find(Notification.class, id);
        if (n != null) s.remove(n);
    }
    
    @Override
    public List<Notification> findAll() {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<Notification> q = s.createQuery(
            "FROM Notification n ORDER BY n.createdAt DESC",
            Notification.class
        );
        return q.getResultList();
    }
}

