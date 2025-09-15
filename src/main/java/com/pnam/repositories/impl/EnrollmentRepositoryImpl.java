package com.pnam.repositories.impl;

import com.pnam.pojo.Enrollment;
import com.pnam.repositories.EnrollmentRepository;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class EnrollmentRepositoryImpl implements EnrollmentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Enrollment findById(Long id) {
        Session s = factory.getObject().getCurrentSession();
        return s.find(Enrollment.class, id);
    }

    @Override
    public List<Enrollment> findByStudent(Long studentId) {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<Enrollment> q = s.createQuery(
            "SELECT e FROM Enrollment e WHERE e.studentId.id = :sid", Enrollment.class);
        q.setParameter("sid", studentId);
        return q.getResultList();
    }

    @Override
    public List<Enrollment> findByCourse(Long courseId) {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<Enrollment> q = s.createQuery(
            "SELECT e FROM Enrollment e WHERE e.courseId.id = :cid", Enrollment.class);
        q.setParameter("cid", courseId);
        return q.getResultList();
    }

    @Override
    public Enrollment findByCourseAndStudent(Long courseId, Long studentId) {
        Session s = factory.getObject().getCurrentSession();
        TypedQuery<Enrollment> q = s.createQuery(
            "SELECT e FROM Enrollment e WHERE e.courseId.id = :cid AND e.studentId.id = :sid",
            Enrollment.class);
        q.setParameter("cid", courseId);
        q.setParameter("sid", studentId);
        return q.getResultStream().findFirst().orElse(null);
    }

    @Override
    public Enrollment save(Enrollment e) {
        Session s = factory.getObject().getCurrentSession();
        if (e.getId() == null) {
            s.persist(e);   // thêm mới
            return e;
        } else {
            return s.merge(e); // cập nhật
        }
    }

    @Override
    public void delete(Long id) {
        Session s = factory.getObject().getCurrentSession();
        Enrollment e = s.find(Enrollment.class, id);
        if (e != null) {
            s.remove(e);
        }
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        Session s = factory.getObject().getCurrentSession();
        return s.createQuery("SELECT e FROM Enrollment e", Enrollment.class).getResultList();
    }
    
}
