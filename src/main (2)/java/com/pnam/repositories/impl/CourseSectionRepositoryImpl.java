package com.pnam.repositories.impl;

import com.pnam.pojo.CourseSection;
import com.pnam.repositories.CourseSectionRepository;
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
public class CourseSectionRepositoryImpl extends BaseRepository<CourseSection, Long>
        implements CourseSectionRepository {

    @Override
    protected Class<CourseSection> getEntityClass() {
        return CourseSection.class;
    }

    @Override
    public CourseSection findById(Long id) {
        return super.findById(id);
    }

    @Override
    public void save(CourseSection section) {
        super.save(section, section.getId());
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public List<CourseSection> getSections(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<CourseSection> cq = cb.createQuery(CourseSection.class);
        Root<CourseSection> root = cq.from(CourseSection.class);
        cq.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                predicates.add(cb.like(root.get("title"), "%" + kw + "%"));
            }

            String courseId = params.get("courseId");
            if (courseId != null && !courseId.isBlank()) {
                predicates.add(cb.equal(root.get("courseId").get("id"), Long.parseLong(courseId)));
            }
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        cq.orderBy(cb.asc(root.get("orderIndex")));

        Query<CourseSection> query = s.createQuery(cq);

        int page = getPage(params);
        int pageSize = getPageSize(params);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public long countSections(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<CourseSection> root = cq.from(CourseSection.class);
        cq.select(cb.count(root));

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                predicates.add(cb.like(root.get("title"), "%" + kw + "%"));
            }

            String courseId = params.get("courseId");
            if (courseId != null && !courseId.isBlank()) {
                predicates.add(cb.equal(root.get("courseId").get("id"), Long.parseLong(courseId)));
            }
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        return s.createQuery(cq).getSingleResult();
    }
}
