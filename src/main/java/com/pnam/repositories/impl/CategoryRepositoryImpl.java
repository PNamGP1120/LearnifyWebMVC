package com.pnam.repositories.impl;

import com.pnam.pojo.Category;
import com.pnam.repositories.CategoryRepository;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class CategoryRepositoryImpl implements CategoryRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session s() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public List<Category> getCategories(Map<String, String> params) {
        Session session = s();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Category> q = b.createQuery(Category.class);
        Root<Category> root = q.from(Category.class);
        q.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.or(
                        b.like(root.get("name"), "%" + kw + "%"),
                        b.like(root.get("slug"), "%" + kw + "%")
                ));
            }
        }
        if (!predicates.isEmpty()) {
            q.where(predicates.toArray(Predicate[]::new));
        }
        q.orderBy(b.asc(root.get("name")));

        Query<Category> query = session.createQuery(q);
        return query.getResultList();
    }

    @Override
    public long countCategories(Map<String, String> params) {
        Session session = s();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<Category> root = q.from(Category.class);
        q.select(b.count(root));

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.or(
                        b.like(root.get("name"), "%" + kw + "%"),
                        b.like(root.get("slug"), "%" + kw + "%")
                ));
            }
        }
        if (!predicates.isEmpty()) {
            q.where(predicates.toArray(Predicate[]::new));
        }

        return session.createQuery(q).getSingleResult();
    }

    @Override
    public Category findById(Long id) {
        return s().find(Category.class, id);
    }

    @Override
    public void save(Category c) {
        if (c.getId() == null) {
            s().persist(c);
        } else {
            s().merge(c);
        }
    }

    @Override
    public void delete(Long id) {
        Category c = findById(id);
        if (c != null) {
            s().remove(c);
        }
    }
}
