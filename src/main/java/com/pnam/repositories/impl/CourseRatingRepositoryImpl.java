package com.pnam.repositories.impl;

import com.pnam.pojo.CourseRating;
import com.pnam.repositories.CourseRatingRepository;
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
public class CourseRatingRepositoryImpl extends BaseRepository<CourseRating, Long>
        implements CourseRatingRepository {

    @Override
    protected Class<CourseRating> getEntityClass() {
        return CourseRating.class;
    }

    @Override
    public List<CourseRating> getRatings(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<CourseRating> cq = cb.createQuery(CourseRating.class);
        Root<CourseRating> root = cq.from(CourseRating.class);
        cq.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            // lọc theo keyword trong comment
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                predicates.add(cb.like(root.get("comment"), "%" + kw + "%"));
            }

            // lọc theo courseId
            String courseId = params.get("courseId");
            if (courseId != null && !courseId.isBlank()) {
                predicates.add(cb.equal(root.get("courseId").get("id"), Long.valueOf(courseId)));
            }
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(Predicate[]::new));
        }

        cq.orderBy(cb.desc(root.get("createdAt")));

        Query<CourseRating> query = s.createQuery(cq);

        int page = getPage(params);
        int pageSize = getPageSize(params);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public long countRatings(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<CourseRating> root = cq.from(CourseRating.class);
        cq.select(cb.count(root));

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                predicates.add(cb.like(root.get("comment"), "%" + kw + "%"));
            }

            String courseId = params.get("courseId");
            if (courseId != null && !courseId.isBlank()) {
                predicates.add(cb.equal(root.get("courseId").get("id"), Long.valueOf(courseId)));
            }
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(Predicate[]::new));
        }

        return s.createQuery(cq).getSingleResult();
    }

    @Override
    public CourseRating findById(Long id) {
        return super.findById(id);
    }

    @Override
    public void save(CourseRating rating) {
        super.save(rating, rating.getId());
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }
}
