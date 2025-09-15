package com.pnam.repositories.impl;

import com.pnam.pojo.Cart;
import com.pnam.repositories.CartRepository;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class CartRepositoryImpl implements CartRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Cart findById(Long id) {
        return factory.getObject().getCurrentSession().find(Cart.class, id);
    }

    @Override
    public List<Cart> findByStudent(Long studentId) {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<Cart> q = s.createQuery(
            "SELECT c FROM Cart c WHERE c.studentId.id = :sid", Cart.class);
        q.setParameter("sid", studentId);
        return q.getResultList();
    }

    @Override
    public Cart save(Cart c) {
        Session s = factory.getObject().getCurrentSession();
        if (c.getId() == null) {
            s.persist(c);
            return c;
        } else {
            return s.merge(c);
        }
    }

    @Override
    public void delete(Long id) {
        Session s = factory.getObject().getCurrentSession();
        Cart c = s.find(Cart.class, id);
        if (c != null) s.remove(c);
    }
}
