package com.pnam.repositories.impl;

import com.pnam.pojo.User;
import com.pnam.repositories.UserRepository;
import com.pnam.repositories.base.BaseRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class UserRepositoryImpl extends BaseRepository<User, Long>
        implements UserRepository {

    private final BCryptPasswordEncoder passwordEncoder;

    public UserRepositoryImpl(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    public User getUserByUsername(String username) {
        try {
            Session s = getSession();
            Query q = s.createNamedQuery("User.findByUsername", User.class);
            q.setParameter("username", username);
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            Session s = getSession();
            Query q = s.createNamedQuery("User.findByEmail", User.class);
            q.setParameter("email", email);
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void addUser(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        getSession().persist(user);
    }

    @Override
    public void updateUser(User user) {
        getSession().merge(user);
    }

    @Override
    public void deleteUser(Long id) {
        super.delete(id);
    }

    @Override
    public User getUserById(Long id) {
        return super.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        Session s = getSession();
        Query q = s.createNamedQuery("User.findAll", User.class);
        return q.getResultList();
    }

    @Override
    public List<User> getUsers(Map<String, String> params) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                Predicate p1 = cb.like(root.get("username"), "%" + kw + "%");
                Predicate p2 = cb.like(root.get("email"), "%" + kw + "%");
                Predicate p3 = cb.like(root.get("fullName"), "%" + kw + "%");
                predicates.add(cb.or(p1, p2, p3));
            }

            String role = params.get("role");
            if (role != null && !role.isBlank()) {
                predicates.add(cb.equal(root.get("role"), role));
            }

            String status = params.get("status");
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        jakarta.persistence.Query query = s.createQuery(cq);

        int page = getPage(params);
        int pageSize = getPageSize(params);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }
}
