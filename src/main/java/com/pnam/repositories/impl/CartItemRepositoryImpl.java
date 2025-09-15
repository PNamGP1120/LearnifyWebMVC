package com.pnam.repositories.impl;

import com.pnam.pojo.CartItem;
import com.pnam.repositories.CartItemRepository;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class CartItemRepositoryImpl implements CartItemRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public CartItem findById(Long id) {
        return factory.getObject().getCurrentSession().find(CartItem.class, id);
    }

    @Override
    public List<CartItem> findByCart(Long cartId) {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<CartItem> q = s.createQuery(
            "SELECT ci FROM CartItem ci WHERE ci.cartId.id = :cid", CartItem.class);
        q.setParameter("cid", cartId);
        return q.getResultList();
    }

    @Override
    public CartItem findByCartAndCourse(Long cartId, Long courseId) {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<CartItem> q = s.createQuery(
            "SELECT ci FROM CartItem ci WHERE ci.cartId.id = :cid AND ci.courseId.id = :coid",
            CartItem.class);
        q.setParameter("cid", cartId);
        q.setParameter("coid", courseId);
        return q.getResultStream().findFirst().orElse(null);
    }

    @Override
    public CartItem save(CartItem ci) {
        Session s = factory.getObject().getCurrentSession();
        if (ci.getId() == null) {
            s.persist(ci);
            return ci;
        } else {
            return s.merge(ci);
        }
    }

    @Override
    public void delete(Long id) {
        Session s = factory.getObject().getCurrentSession();
        CartItem ci = s.find(CartItem.class, id);
        if (ci != null) s.remove(ci);
    }
}
