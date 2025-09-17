/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pnam.repositories.impl;

import com.pnam.pojo.ChatMessage;
import com.pnam.repositories.ChatMessageRepository;
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
public class ChatMessageRepositoryImpl implements ChatMessageRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public ChatMessage findById(Long id) {
        return factory.getObject().getCurrentSession().find(ChatMessage.class, id);
    }

    @Override
    public List<ChatMessage> findByThread(Long threadId) {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<ChatMessage> q = s.createQuery(
            "SELECT m FROM ChatMessage m WHERE m.threadId.id = :tid ORDER BY m.sentAt ASC",
            ChatMessage.class);
        q.setParameter("tid", threadId);
        return q.getResultList();
    }

    @Override
    public ChatMessage save(ChatMessage m) {
        Session s = factory.getObject().getCurrentSession();
        if (m.getId() == null) {
            s.persist(m);
            return m;
        } else {
            return s.merge(m);
        }
    }

    @Override
    public void delete(Long id) {
        Session s = factory.getObject().getCurrentSession();
        ChatMessage m = s.find(ChatMessage.class, id);
        if (m != null) s.remove(m);
    }
}

