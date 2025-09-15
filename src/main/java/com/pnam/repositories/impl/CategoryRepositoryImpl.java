package com.pnam.repositories.impl;

import com.pnam.pojo.Category;
import com.pnam.repositories.CategoryRepository;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class CategoryRepositoryImpl implements CategoryRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Category findById(Long id) {
        Session s = factory.getObject().getCurrentSession();
        return s.find(Category.class, id);
    }

    @Override
    public Category findBySlug(String slug) {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<Category> q = s.createQuery(
            "SELECT c FROM Category c WHERE c.slug = :slug", Category.class);
        q.setParameter("slug", slug);
        return q.getResultStream().findFirst().orElse(null);
    }

    @Override
    public List<Category> findAll() {
        Session s = factory.getObject().getCurrentSession();
        return s.createQuery("SELECT c FROM Category c ORDER BY c.name ASC", Category.class)
                .getResultList();
    }

    @Override
    public Category save(Category c) {
        Session s = factory.getObject().getCurrentSession();
        if (c.getId() == null) {
            s.persist(c); // thêm mới
            return c;
        } else {
            return s.merge(c); // cập nhật
        }
    }

    @Override
    public void delete(Long id) {
        Session s = factory.getObject().getCurrentSession();
        Category c = s.find(Category.class, id);
        if (c != null) s.remove(c);
    }
}
