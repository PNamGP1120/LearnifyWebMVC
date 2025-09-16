package com.pnam.repositories.impl;

import com.pnam.pojo.ChatThread;
import com.pnam.repositories.ChatThreadRepository;
import com.pnam.repositories.base.BaseRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class ChatThreadRepositoryImpl extends BaseRepository<ChatThread, Long>
        implements ChatThreadRepository {

    @Override
    protected Class<ChatThread> getEntityClass() {
        return ChatThread.class;
    }

    @Override
    public ChatThread findById(Long id) {
        return super.findById(id);
    }

    @Override
    public ChatThread save(ChatThread ct) {
        return super.save(ct, ct.getId());
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public List<ChatThread> findByCourse(Long courseId) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<ChatThread> cq = cb.createQuery(ChatThread.class);
        Root<ChatThread> root = cq.from(ChatThread.class);
        cq.select(root);

        cq.where(cb.equal(root.get("courseId").get("id"), courseId));

        Query<ChatThread> query = s.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<ChatThread> findByUsers(Long userAId, Long userBId) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<ChatThread> cq = cb.createQuery(ChatThread.class);
        Root<ChatThread> root = cq.from(ChatThread.class);
        cq.select(root);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.and(
                cb.equal(root.get("userA").get("id"), userAId),
                cb.equal(root.get("userB").get("id"), userBId)
        ));
        predicates.add(cb.and(
                cb.equal(root.get("userA").get("id"), userBId),
                cb.equal(root.get("userB").get("id"), userAId)
        ));

        cq.where(cb.or(predicates.toArray(new Predicate[0])));

        Query<ChatThread> query = s.createQuery(cq);
        return query.getResultList();
    }
}
