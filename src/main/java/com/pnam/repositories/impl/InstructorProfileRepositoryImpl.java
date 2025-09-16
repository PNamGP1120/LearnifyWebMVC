package com.pnam.repositories.impl;

import com.pnam.pojo.InstructorProfile;
import com.pnam.repositories.InstructorProfileRepository;
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
public class InstructorProfileRepositoryImpl extends BaseRepository<InstructorProfile, Long>
        implements InstructorProfileRepository {

    @Override
    protected Class<InstructorProfile> getEntityClass() {
        return InstructorProfile.class;
    }

    @Override
    public List<InstructorProfile> getProfiles(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<InstructorProfile> cq = cb.createQuery(InstructorProfile.class);
        Root<InstructorProfile> root = cq.from(InstructorProfile.class);
        cq.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                Predicate p1 = cb.like(root.get("bio"), "%" + kw + "%");
                Predicate p2 = cb.like(root.get("certifications"), "%" + kw + "%");
                predicates.add(cb.or(p1, p2));
            }

            String verified = params.get("verified");
            if (verified != null && !verified.isBlank()) {
                predicates.add(cb.equal(root.get("verifiedByAdmin"), Boolean.parseBoolean(verified)));
            }
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        Query<InstructorProfile> query = s.createQuery(cq);

        int page = getPage(params);
        int pageSize = getPageSize(params);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public long countProfiles(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<InstructorProfile> root = cq.from(InstructorProfile.class);
        cq.select(cb.count(root));

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                Predicate p1 = cb.like(root.get("bio"), "%" + kw + "%");
                Predicate p2 = cb.like(root.get("certifications"), "%" + kw + "%");
                predicates.add(cb.or(p1, p2));
            }

            String verified = params.get("verified");
            if (verified != null && !verified.isBlank()) {
                predicates.add(cb.equal(root.get("verifiedByAdmin"), Boolean.parseBoolean(verified)));
            }
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        return s.createQuery(cq).getSingleResult();
    }

    @Override
    public InstructorProfile findById(Long userId) {
        return super.findById(userId);
    }

    @Override
    public void save(InstructorProfile instructor) {
        super.save(instructor, instructor.getUserId());
    }

    @Override
    public void delete(Long userId) {
        super.delete(userId);
    }
}
