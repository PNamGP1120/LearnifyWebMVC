package com.pnam.repositories.impl;

import com.pnam.pojo.AuditLog;
import com.pnam.repositories.AuditLogRepository;
import com.pnam.repositories.base.BaseRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
public class AuditLogRepositoryImpl extends BaseRepository<AuditLog, Long>
        implements AuditLogRepository {

    @Override
    protected Class<AuditLog> getEntityClass() {
        return AuditLog.class;
    }

    @Override
    public AuditLog findById(Long id) {
        return super.findById(id);
    }

    @Override
    public AuditLog save(AuditLog log) {
        return super.save(log, log.getId());
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public List<AuditLog> findByUser(Long userId) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<AuditLog> cq = cb.createQuery(AuditLog.class);
        Root<AuditLog> root = cq.from(AuditLog.class);
        cq.select(root);

        cq.where(cb.equal(root.get("userId").get("id"), userId));
        cq.orderBy(cb.desc(root.get("createdAt")));

        Query<AuditLog> query = s.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<AuditLog> findAll() {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<AuditLog> cq = cb.createQuery(AuditLog.class);
        Root<AuditLog> root = cq.from(AuditLog.class);
        cq.select(root);
        cq.orderBy(cb.desc(root.get("createdAt")));

        Query<AuditLog> query = s.createQuery(cq);
        return query.getResultList();
    }
}
