package com.pnam.repositories.impl;

import com.pnam.pojo.*;
import com.pnam.repositories.StatsRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class StatsRepositoryImpl implements StatsRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getSession() {
        return factory.getObject().getCurrentSession();
    }

    private Date parseDate(String s) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(s);
        } catch (Exception e) {
            return null;
        }
    }

    // ==== ADMIN ====

    @Override
    public List<Object[]> statsUsersByRole(Map<String, String> filters) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        Root<User> root = q.from(User.class);

        q.multiselect(root.get("role"), b.count(root.get("id")));
        q.groupBy(root.get("role"));

        if (filters != null && filters.containsKey("status")) {
            q.where(b.equal(root.get("status"), filters.get("status")));
        }

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> statsCoursesByCategory(Map<String, String> filters) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        Root<Course> root = q.from(Course.class);
        Join<Course, Category> join = root.join("categoryId", JoinType.INNER);

        q.multiselect(join.get("name"), b.count(root.get("id")));
        q.groupBy(join.get("name"));

        if (filters != null && filters.containsKey("status")) {
            q.where(b.equal(root.get("status"), filters.get("status")));
        }

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> statsRevenueByMonth(Map<String, String> filters) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        Root<Payment> root = q.from(Payment.class);

        Expression<Integer> monthExp = b.function("MONTH", Integer.class, root.get("createdAt"));
        Expression<Integer> yearExp = b.function("YEAR", Integer.class, root.get("createdAt"));

        q.multiselect(monthExp, b.sum(root.get("amount")));

        List<Predicate> predicates = new ArrayList<>();

        if (filters != null) {
            if (filters.containsKey("year")) {
                predicates.add(b.equal(yearExp, Integer.parseInt(filters.get("year"))));
            }
            if (filters.containsKey("fromDate") && filters.containsKey("toDate")) {
                Date from = parseDate(filters.get("fromDate"));
                Date to = parseDate(filters.get("toDate"));
                if (from != null && to != null) {
                    predicates.add(b.between(root.get("createdAt"), from, to));
                }
            }
            if (filters.containsKey("method")) {
                predicates.add(b.equal(root.get("method"), filters.get("method")));
            }
        }

        if (!predicates.isEmpty())
            q.where(b.and(predicates.toArray(new Predicate[0])));

        q.groupBy(monthExp);
        q.orderBy(b.asc(monthExp));

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> statsTopInstructorsByRevenue(Map<String, String> filters, int limit) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        Root<Payment> root = q.from(Payment.class);

        Join<Payment, Enrollment> joinE = root.join("enrollmentId", JoinType.INNER);
        Join<Enrollment, Course> joinC = joinE.join("courseId", JoinType.INNER);
        Join<Course, User> joinU = joinC.join("instructorId", JoinType.INNER);

        q.multiselect(joinU.get("fullName"), b.sum(root.get("amount")));
        q.groupBy(joinU.get("fullName"));
        q.orderBy(b.desc(b.sum(root.get("amount"))));

        if (filters != null && filters.containsKey("year")) {
            Expression<Integer> yearExp = b.function("YEAR", Integer.class, root.get("createdAt"));
            q.where(b.equal(yearExp, Integer.parseInt(filters.get("year"))));
        }

        Query query = s.createQuery(q);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public List<Object[]> statsTopCoursesByEnrollments(Map<String, String> filters, int limit) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);

        Root<Enrollment> root = q.from(Enrollment.class);
        Join<Enrollment, Course> joinC = root.join("courseId", JoinType.INNER);

        q.multiselect(joinC.get("title"), b.count(root.get("id")));
        q.groupBy(joinC.get("title"));
        q.orderBy(b.desc(b.count(root.get("id"))));

        if (filters != null && filters.containsKey("categoryId")) {
            q.where(b.equal(joinC.get("categoryId").get("id"), Long.parseLong(filters.get("categoryId"))));
        }

        Query query = s.createQuery(q);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    // ==== INSTRUCTOR ====

    @Override
    public List<Object[]> statsEnrollmentsByCourse(Map<String, String> filters) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        Root<Enrollment> root = q.from(Enrollment.class);
        Join<Enrollment, Course> joinC = root.join("courseId", JoinType.INNER);

        q.multiselect(joinC.get("title"), b.count(root.get("id")));
        q.groupBy(joinC.get("title"));

        if (filters != null && filters.containsKey("instructorId")) {
            q.where(b.equal(joinC.get("instructorId").get("id"),
                    Long.parseLong(filters.get("instructorId"))));
        }

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> statsRevenueByCourse(Map<String, String> filters) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);

        Root<Payment> root = q.from(Payment.class);
        Join<Payment, Enrollment> joinE = root.join("enrollmentId", JoinType.INNER);
        Join<Enrollment, Course> joinC = joinE.join("courseId", JoinType.INNER);

        q.multiselect(joinC.get("title"), b.sum(root.get("amount")));
        q.groupBy(joinC.get("title"));

        if (filters != null && filters.containsKey("instructorId")) {
            q.where(b.equal(joinC.get("instructorId").get("id"),
                    Long.parseLong(filters.get("instructorId"))));
        }

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> statsRevenueByMonthForInstructor(Map<String, String> filters) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);

        Root<Payment> root = q.from(Payment.class);
        Join<Payment, Enrollment> joinE = root.join("enrollmentId", JoinType.INNER);
        Join<Enrollment, Course> joinC = joinE.join("courseId", JoinType.INNER);

        Expression<Integer> monthExp = b.function("MONTH", Integer.class, root.get("createdAt"));
        Expression<Integer> yearExp = b.function("YEAR", Integer.class, root.get("createdAt"));

        q.multiselect(monthExp, b.sum(root.get("amount")));
        List<Predicate> predicates = new ArrayList<>();

        if (filters != null) {
            if (filters.containsKey("year")) {
                predicates.add(b.equal(yearExp, Integer.parseInt(filters.get("year"))));
            }
            if (filters.containsKey("instructorId")) {
                predicates.add(b.equal(joinC.get("instructorId").get("id"),
                        Long.parseLong(filters.get("instructorId"))));
            }
        }

        if (!predicates.isEmpty())
            q.where(b.and(predicates.toArray(new Predicate[0])));

        q.groupBy(monthExp);
        q.orderBy(b.asc(monthExp));

        return s.createQuery(q).getResultList();
    }

    // ==== STUDENT ====

    @Override
    public List<Object[]> statsCoursesByStudent(Map<String, String> filters) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);

        Root<Enrollment> root = q.from(Enrollment.class);
        Join<Enrollment, Course> joinC = root.join("courseId", JoinType.INNER);

        q.multiselect(joinC.get("title"), root.get("enrolledAt"));

        if (filters != null && filters.containsKey("studentId")) {
            q.where(b.equal(root.get("studentId").get("id"),
                    Long.parseLong(filters.get("studentId"))));
        }

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> statsProgressByStudent(Map<String, String> filters) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);

        Root<Progress> root = q.from(Progress.class);
        Join<Progress, Course> joinC = root.join("courseId", JoinType.INNER);

        q.multiselect(joinC.get("title"), root.get("progressPercent"));

        if (filters != null && filters.containsKey("studentId")) {
            q.where(b.equal(root.get("studentId").get("id"),
                    Long.parseLong(filters.get("studentId"))));
        }

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> statsPaymentsByStudent(Map<String, String> filters) {
        Session s = getSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);

        Root<Payment> root = q.from(Payment.class);
        Join<Payment, Enrollment> joinE = root.join("enrollmentId", JoinType.INNER);

        q.multiselect(root.get("method"), b.sum(root.get("amount")));
        q.groupBy(root.get("method"));

        if (filters != null && filters.containsKey("studentId")) {
            q.where(b.equal(joinE.get("studentId").get("id"),
                    Long.parseLong(filters.get("studentId"))));
        }

        return s.createQuery(q).getResultList();
    }
}
