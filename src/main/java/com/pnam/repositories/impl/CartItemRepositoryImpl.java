package com.pnam.repositories.impl;

import com.pnam.pojo.CartItem;
import com.pnam.repositories.CartItemRepository;
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
public class CartItemRepositoryImpl extends BaseRepository<CartItem, Long>
        implements CartItemRepository {

    @Override
    protected Class<CartItem> getEntityClass() {
        return CartItem.class;
    }

    @Override
    public CartItem findById(Long id) {
        return super.findById(id);
    }

    @Override
    public CartItem save(CartItem ci) {
        return super.save(ci, ci.getId());
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public List<CartItem> findByCart(Long cartId) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<CartItem> cq = cb.createQuery(CartItem.class);
        Root<CartItem> root = cq.from(CartItem.class);
        cq.select(root);

        cq.where(cb.equal(root.get("cartId").get("id"), cartId));

        Query<CartItem> query = s.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public CartItem findByCartAndCourse(Long cartId, Long courseId) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<CartItem> cq = cb.createQuery(CartItem.class);
        Root<CartItem> root = cq.from(CartItem.class);
        cq.select(root);

        Predicate p1 = cb.equal(root.get("cartId").get("id"), cartId);
        Predicate p2 = cb.equal(root.get("courseId").get("id"), courseId);
        cq.where(cb.and(p1, p2));

        Query<CartItem> query = s.createQuery(cq);
        return query.getResultStream().findFirst().orElse(null);
    }
}
