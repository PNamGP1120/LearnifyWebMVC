package com.pnam.repositories.impl;

import com.pnam.pojo.InstructorProfile;
import com.pnam.repositories.InstructorProfileRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class InstructorProfileRepositoryImpl implements InstructorProfileRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<InstructorProfile> getProfiles(Map<String, String> params) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<InstructorProfile> cq = cb.createQuery(InstructorProfile.class);
        Root<InstructorProfile> root = cq.from(InstructorProfile.class);
        cq.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                Predicate p1 = cb.like(root.get("bio"), "%" + kw + "%");
                Predicate p2 = cb.like(root.get("certifications"), "%" + kw + "%");
                predicates.add(cb.or(p1, p2));
            }

            String verified = params.get("verified");
            if (verified != null && !verified.isEmpty()) {
                predicates.add(cb.equal(root.get("verifiedByAdmin"), Boolean.parseBoolean(verified)));
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));
        Query<InstructorProfile> query = s.createQuery(cq);

        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.get("page"));
            int pageSize = params.containsKey("pageSize") ? Integer.parseInt(params.get("pageSize")) : 10;
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
        }

        return query.getResultList();
    }

    @Override
    public long countProfiles(Map<String, String> params) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<InstructorProfile> root = cq.from(InstructorProfile.class);
        cq.select(cb.count(root));

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                Predicate p1 = cb.like(root.get("bio"), "%" + kw + "%");
                Predicate p2 = cb.like(root.get("certifications"), "%" + kw + "%");
                predicates.add(cb.or(p1, p2));
            }

            String verified = params.get("verified");
            if (verified != null && !verified.isEmpty()) {
                predicates.add(cb.equal(root.get("verifiedByAdmin"), Boolean.parseBoolean(verified)));
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return s.createQuery(cq).getSingleResult();
    }

    @Override
    public InstructorProfile findById(Long userId) {
        Session s = factory.getObject().getCurrentSession();
        return s.find(InstructorProfile.class, userId);
    }

    @Override
    public void save(InstructorProfile instructor) {
        Session s = factory.getObject().getCurrentSession();
        if (findById(instructor.getUserId()) == null) {
            s.persist(instructor);
        } else {
            s.merge(instructor);
        }
    }

    @Override
    public void delete(Long userId) {
        Session s = factory.getObject().getCurrentSession();
        InstructorProfile ip = s.find(InstructorProfile.class, userId);
        if (ip != null) {
            s.remove(ip);
        }
    }
}
