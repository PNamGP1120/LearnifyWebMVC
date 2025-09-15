package com.pnam.repositories.impl;

import com.pnam.pojo.User;
import com.pnam.repositories.UserRepository;
import jakarta.persistence.NoResultException;
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
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    @Override
    public User getUserByUsername(String username) {
        Session s = sessionFactory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM User WHERE username = :un");
        q.setParameter("un", username);
        try {
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        Session s = sessionFactory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM User WHERE email = :em");
        q.setParameter("em", email);
        try {
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void addUser(User user) {
        Session s = sessionFactory.getObject().getCurrentSession();
        s.persist(user);
    }

    @Override
    public void updateUser(User user) {
        Session s = sessionFactory.getObject().getCurrentSession();
        s.merge(user);
    }

    @Override
    public void deleteUser(Long id) {
        Session s = sessionFactory.getObject().getCurrentSession();
        User u = s.get(User.class, id);
        if (u != null) {
            s.remove(u);
        }
    }

    @Override
    public User getUserById(Long id) {
        Session s = sessionFactory.getObject().getCurrentSession();
        return s.find(User.class, id);
    }

    @Override
    public List<User> getAllUsers() {
        Session s = sessionFactory.getObject().getCurrentSession();
        Query<User> q = s.createQuery("FROM User", User.class);
        return q.getResultList();
    }

    @Override
    public List<User> getUsers(Map<String, String> params) {
        Session s = sessionFactory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                Predicate p1 = cb.like(root.get("username"), "%" + kw + "%");
                Predicate p2 = cb.like(root.get("email"), "%" + kw + "%");
                Predicate p3 = cb.like(root.get("fullName"), "%" + kw + "%");
                predicates.add(cb.or(p1, p2, p3));
            }

            String role = params.get("role");
            if (role != null && !role.isEmpty()) {
                predicates.add(cb.equal(root.get("role"), role));
            }

            String status = params.get("status");
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
        }

        cq.where(predicates.toArray(Predicate[]::new));
        Query<User> query = s.createQuery(cq);

        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.get("page"));
            int pageSize = params.containsKey("pageSize")
                    ? Integer.parseInt(params.get("pageSize"))
                    : 10;
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
        }

        return query.getResultList();
    }
}
