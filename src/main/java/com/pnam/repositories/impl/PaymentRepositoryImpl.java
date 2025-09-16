package com.pnam.repositories.impl;

import com.pnam.pojo.Payment;
import com.pnam.repositories.PaymentRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class PaymentRepositoryImpl implements PaymentRepository {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public List<Payment> getPayments(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Payment> q = b.createQuery(Payment.class);
        Root<Payment> root = q.from(Payment.class);
        q.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String enrollmentId = params.get("enrollmentId");
            if (enrollmentId != null && !enrollmentId.isBlank()) {
                predicates.add(b.equal(root.get("enrollmentId").get("id"), Long.parseLong(enrollmentId)));
            }

            String status = params.get("status");
            if (status != null && !status.isBlank()) {
                predicates.add(b.equal(root.get("status"), status));
            }
        }

        q.where(predicates.toArray(Predicate[]::new));
        q.orderBy(b.desc(root.get("createdAt")));

        Query query = s.createQuery(q);
        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.get("page"));
            query.setFirstResult((page - 1) * PAGE_SIZE);
            query.setMaxResults(PAGE_SIZE);
        }

        return query.getResultList();
    }

    @Override
    public long countPayments(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<Payment> root = q.from(Payment.class);
        q.select(b.count(root));

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String status = params.get("status");
            if (status != null && !status.isBlank()) {
                predicates.add(b.equal(root.get("status"), status));
            }
        }
        q.where(predicates.toArray(Predicate[]::new));

        return s.createQuery(q).getSingleResult();
    }

    @Override
    public Payment findById(Long id) {
        return getSession().get(Payment.class, id);
    }

    @Override
    public void save(Payment p) {
        Session s = getSession();
        if (p.getId() == null) {
            s.persist(p);
        } else {
            s.merge(p);
        }
    }

    @Override
    public void delete(Long id) {
        Payment p = findById(id);
        if (p != null) {
            getSession().remove(p);
        }
    }
}
