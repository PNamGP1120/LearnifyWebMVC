package com.pnam.repositories.impl;

import com.pnam.pojo.Category;
import com.pnam.repositories.CategoryRepository;
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
public class CategoryRepositoryImpl extends BaseRepository<Category, Long>
        implements CategoryRepository {

    @Override
    protected Class<Category> getEntityClass() {
        return Category.class;
    }

    @Override
    public List<Category> getCategories(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Category> cq = cb.createQuery(Category.class);
        Root<Category> root = cq.from(Category.class);
        cq.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                predicates.add(cb.or(
                        cb.like(root.get("name"), "%" + kw + "%"),
                        cb.like(root.get("slug"), "%" + kw + "%")
                ));
            }
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        cq.orderBy(cb.asc(root.get("name")));

        Query<Category> query = s.createQuery(cq);

        // phân trang
        int page = getPage(params);
        int pageSize = getPageSize(params);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public long countCategories(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Category> root = cq.from(Category.class);
        cq.select(cb.count(root));

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                predicates.add(cb.or(
                        cb.like(root.get("name"), "%" + kw + "%"),
                        cb.like(root.get("slug"), "%" + kw + "%")
                ));
            }
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        return s.createQuery(cq).getSingleResult();
    }

    @Override
    public Category findById(Long id) {
        return super.findById(id);
    }

    @Override
    public void save(Category c) {
        super.save(c, c.getId());
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }
}
