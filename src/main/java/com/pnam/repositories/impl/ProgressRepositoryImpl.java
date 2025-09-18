package com.pnam.repositories.impl;

import com.pnam.pojo.Progress;
import com.pnam.repositories.ProgressRepository;
import com.pnam.repositories.base.BaseRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
public class ProgressRepositoryImpl extends BaseRepository<Progress, Long>
        implements ProgressRepository {

    @Override
    protected Class<Progress> getEntityClass() {
        return Progress.class;
    }

    @Override
    public Progress findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Progress save(Progress p) {
        return super.save(p, p.getId());
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public List<Progress> findByEnrollment(Long enrollmentId) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Progress> cq = cb.createQuery(Progress.class);
        Root<Progress> root = cq.from(Progress.class);
        cq.select(root);

        cq.where(cb.equal(root.get("enrollmentId").get("id"), enrollmentId));
        cq.orderBy(cb.asc(root.get("id")));

        Query<Progress> query = s.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public long countLessons(Long enrollmentId) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Progress> root = cq.from(Progress.class);

        Predicate byEnrollment = cb.equal(root.get("enrollmentId").get("id"), enrollmentId);

        cq.select(cb.count(root)).where(byEnrollment);

        return s.createQuery(cq).getSingleResult();
    }

    @Override
    public long countCompletedLessons(Long enrollmentId) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Progress> root = cq.from(Progress.class);

        Predicate byEnrollment = cb.equal(root.get("enrollmentId").get("id"), enrollmentId);
        Predicate completed = cb.isTrue(root.get("completed"));

        cq.select(cb.count(root)).where(cb.and(byEnrollment, completed));

        return s.createQuery(cq).getSingleResult();
    }
}
