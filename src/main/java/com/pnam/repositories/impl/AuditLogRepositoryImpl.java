 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pnam.repositories.impl;

import com.pnam.pojo.AuditLog;
import com.pnam.repositories.AuditLogRepository;
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
public class AuditLogRepositoryImpl implements AuditLogRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public AuditLog findById(Long id) {
        return factory.getObject().getCurrentSession().find(AuditLog.class, id);
    }

    @Override
    public List<AuditLog> findByUser(Long userId) {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<AuditLog> q = s.createQuery(
            "SELECT a FROM AuditLog a WHERE a.userId.id = :uid ORDER BY a.createdAt DESC",
            AuditLog.class);
        q.setParameter("uid", userId);
        return q.getResultList();
    }

    @Override
    public AuditLog save(AuditLog log) {
        Session s = factory.getObject().getCurrentSession();
        if (log.getId() == null) {
            s.persist(log);
            return log;
        } else {
            return s.merge(log);
        }
    }

    @Override
    public void delete(Long id) {
        Session s = factory.getObject().getCurrentSession();
        AuditLog log = s.find(AuditLog.class, id);
        if (log != null) s.remove(log);
    }
    
    @Override
    public List<AuditLog> findAll() {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<AuditLog> q = s.createQuery(
            "FROM AuditLog a ORDER BY a.createdAt DESC",
            AuditLog.class
        );
        return q.getResultList();
    }
}
