package com.pnam.repositories.impl;

import com.pnam.pojo.Cart;
import com.pnam.repositories.CartRepository;
import com.pnam.repositories.base.BaseRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
public class CartRepositoryImpl extends BaseRepository<Cart, Long>
        implements CartRepository {

    @Override
    protected Class<Cart> getEntityClass() {
        return Cart.class;
    }

    @Override
    public Cart findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Cart save(Cart c) {
        return super.save(c, c.getId());
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public List<Cart> findByStudent(Long studentId) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Cart> cq = cb.createQuery(Cart.class);
        Root<Cart> root = cq.from(Cart.class);
        cq.select(root);

        cq.where(cb.equal(root.get("studentId").get("id"), studentId));

        Query<Cart> query = s.createQuery(cq);
        return query.getResultList();
    }
}
