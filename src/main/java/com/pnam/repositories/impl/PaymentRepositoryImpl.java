package com.pnam.repositories.impl;

import com.pnam.pojo.Payment;
import com.pnam.repositories.PaymentRepository;
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
import java.util.Map;

@Repository
@Transactional
public class PaymentRepositoryImpl extends BaseRepository<Payment, Long>
        implements PaymentRepository {

    @Override
    protected Class<Payment> getEntityClass() {
        return Payment.class;
    }

    @Override
    public List<Payment> getPayments(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Payment> cq = cb.createQuery(Payment.class);
        Root<Payment> root = cq.from(Payment.class);
        cq.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String enrollmentId = params.get("enrollmentId");
            if (enrollmentId != null && !enrollmentId.isBlank()) {
                predicates.add(cb.equal(root.get("enrollmentId").get("id"), Long.parseLong(enrollmentId)));
            }

            String status = params.get("status");
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        cq.orderBy(cb.desc(root.get("createdAt")));

        Query<Payment> query = s.createQuery(cq);
        int page = getPage(params);
        int pageSize = getPageSize(params);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public long countPayments(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Payment> root = cq.from(Payment.class);
        cq.select(cb.count(root));

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String status = params.get("status");
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        return s.createQuery(cq).getSingleResult();
    }

    @Override
    public Payment findById(Long id) {
        return super.findById(id);
    }

    @Override
    public void save(Payment p) {
        super.save(p, p.getId());
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }
}
