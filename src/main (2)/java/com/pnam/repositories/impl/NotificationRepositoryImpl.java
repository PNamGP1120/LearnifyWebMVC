package com.pnam.repositories.impl;

import com.pnam.pojo.Notification;
import com.pnam.repositories.NotificationRepository;
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
public class NotificationRepositoryImpl extends BaseRepository<Notification, Long>
        implements NotificationRepository {

    @Override
    protected Class<Notification> getEntityClass() {
        return Notification.class;
    }

    @Override
    public Notification findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Notification save(Notification n) {
        return super.save(n, n.getId());
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public List<Notification> findByUser(Long userId) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Notification> cq = cb.createQuery(Notification.class);
        Root<Notification> root = cq.from(Notification.class);
        cq.select(root);

        cq.where(cb.equal(root.get("userId").get("id"), userId));
        cq.orderBy(cb.desc(root.get("createdAt")));

        Query<Notification> query = s.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<Notification> findAll() {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Notification> cq = cb.createQuery(Notification.class);
        Root<Notification> root = cq.from(Notification.class);
        cq.select(root);

        cq.orderBy(cb.desc(root.get("createdAt")));

        Query<Notification> query = s.createQuery(cq);
        return query.getResultList();
    }
}
