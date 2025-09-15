/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pnam.repositories.impl;

import com.pnam.pojo.ChatThread;
import com.pnam.repositories.ChatThreadRepository;
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
public class ChatThreadRepositoryImpl implements ChatThreadRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public ChatThread findById(Long id) {
        return factory.getObject().getCurrentSession().find(ChatThread.class, id);
    }

    @Override
    public List<ChatThread> findByCourse(Long courseId) {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<ChatThread> q = s.createQuery(
            "SELECT ct FROM ChatThread ct WHERE ct.courseId.id = :cid", ChatThread.class);
        q.setParameter("cid", courseId);
        return q.getResultList();
    }

    @Override
    public List<ChatThread> findByUsers(Long userAId, Long userBId) {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<ChatThread> q = s.createQuery(
            "SELECT ct FROM ChatThread ct WHERE (ct.userA.id = :u1 AND ct.userB.id = :u2) " +
            "OR (ct.userA.id = :u2 AND ct.userB.id = :u1)", ChatThread.class);
        q.setParameter("u1", userAId);
        q.setParameter("u2", userBId);
        return q.getResultList();
    }

    @Override
    public ChatThread save(ChatThread ct) {
        Session s = factory.getObject().getCurrentSession();
        if (ct.getId() == null) {
            s.persist(ct);
            return ct;
        } else {
            return s.merge(ct);
        }
    }

    @Override
    public void delete(Long id) {
        Session s = factory.getObject().getCurrentSession();
        ChatThread ct = s.find(ChatThread.class, id);
        if (ct != null) s.remove(ct);
    }
}

