/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pnam.repositories.impl;

import com.pnam.repositories.StatsRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author pnam
 */
@Repository
@Transactional
public class StatsRepositoryImpl implements StatsRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getSession() {
        return factory.getObject().getCurrentSession();
    }

    // ===== Users =====
    @Override
    public Map<String, Long> countUsersByRole() {
        Query<Object[]> q = getSession().createQuery(
                "SELECT u.role, COUNT(u) FROM User u GROUP BY u.role", Object[].class);
        Map<String, Long> result = new HashMap<>();
        for (Object[] row : q.getResultList()) {
            result.put((String) row[0], (Long) row[1]);
        }
        return result;
    }

    @Override
    public Long countPendingInstructors() {
        return getSession().createQuery(
                "SELECT COUNT(ip) FROM InstructorProfile ip WHERE ip.verifiedByAdmin = false",
                Long.class).getSingleResult();
    }

    @Override
    public List<Object[]> countUserRegistrationByMonth(int year) {
        return getSession().createQuery(
                "SELECT MONTH(u.createdAt), COUNT(u) FROM User u "
                + "WHERE YEAR(u.createdAt) = :y GROUP BY MONTH(u.createdAt)",
                Object[].class).setParameter("y", year).getResultList();
    }

    // ===== Courses =====
    @Override
    public Map<String, Long> countCoursesByStatus() {
        Query<Object[]> q = getSession().createQuery(
                "SELECT c.status, COUNT(c) FROM Course c GROUP BY c.status", Object[].class);
        Map<String, Long> result = new HashMap<>();
        for (Object[] row : q.getResultList()) {
            result.put((String) row[0], (Long) row[1]);
        }
        return result;
    }

    @Override
    public List<Object[]> countCoursesByCategory() {
        return getSession().createQuery(
                "SELECT c.categoryId.name, COUNT(c) FROM Course c GROUP BY c.categoryId.name",
                Object[].class).getResultList();
    }

    @Override
    public List<Object[]> countCoursesByMonth(int year) {
        return getSession().createQuery(
                "SELECT MONTH(c.createdAt), COUNT(c) FROM Course c "
                + "WHERE YEAR(c.createdAt) = :y GROUP BY MONTH(c.createdAt)",
                Object[].class).setParameter("y", year).getResultList();
    }

    // ===== Enrollments =====
    @Override
    public List<Object[]> countEnrollmentsByCourse() {
        return getSession().createQuery(
                "SELECT e.courseId.title, COUNT(e) FROM Enrollment e GROUP BY e.courseId.title",
                Object[].class).getResultList();
    }

    @Override
    public List<Object[]> countEnrollmentsByInstructor() {
        return getSession().createQuery(
                "SELECT e.courseId.instructorId.fullName, COUNT(e) "
                + "FROM Enrollment e GROUP BY e.courseId.instructorId.fullName",
                Object[].class).getResultList();
    }

    @Override
    public List<Object[]> countEnrollmentsByMonth(int year) {
        return getSession().createQuery(
                "SELECT MONTH(e.enrolledAt), COUNT(e) "
                + "FROM Enrollment e WHERE YEAR(e.enrolledAt) = :y GROUP BY MONTH(e.enrolledAt)",
                Object[].class).setParameter("y", year).getResultList();
    }

    @Override
    public List<Object[]> topCoursesByEnrollments(int limit) {
        return getSession().createQuery(
                "SELECT e.courseId.title, COUNT(e) FROM Enrollment e "
                + "GROUP BY e.courseId.title ORDER BY COUNT(e) DESC",
                Object[].class).setMaxResults(limit).getResultList();
    }

    // ===== Payments =====
    @Override
    public List<Object[]> revenueByCourse() {
        return getSession().createQuery(
                "SELECT e.courseId.title, SUM(p.amount) FROM Payment p JOIN p.enrollmentId e "
                + "WHERE p.status = 'SUCCEEDED' GROUP BY e.courseId.title",
                Object[].class).getResultList();
    }

    @Override
    public List<Object[]> revenueByInstructor() {
        return getSession().createQuery(
                "SELECT e.courseId.instructorId.fullName, SUM(p.amount) "
                + "FROM Payment p JOIN p.enrollmentId e "
                + "WHERE p.status = 'SUCCEEDED' GROUP BY e.courseId.instructorId.fullName",
                Object[].class).getResultList();
    }

    @Override
    public List<Object[]> revenueByMonth(int year) {
        return getSession().createQuery(
                "SELECT MONTH(p.createdAt), SUM(p.amount) FROM Payment p "
                + "WHERE YEAR(p.createdAt) = :y AND p.status = 'SUCCEEDED' "
                + "GROUP BY MONTH(p.createdAt) ORDER BY MONTH(p.createdAt)",
                Object[].class).setParameter("y", year).getResultList();
    }

    @Override
    public Map<String, Long> countPaymentsByStatus() {
        Query<Object[]> q = getSession().createQuery(
                "SELECT p.status, COUNT(p) FROM Payment p GROUP BY p.status", Object[].class);
        Map<String, Long> result = new HashMap<>();
        for (Object[] row : q.getResultList()) {
            result.put((String) row[0], (Long) row[1]);
        }
        return result;
    }

    // ===== Ratings =====
    @Override
    public List<Object[]> avgRatingByCourse() {
        return getSession().createQuery(
                "SELECT r.courseId.title, AVG(r.rating) FROM CourseRating r GROUP BY r.courseId.title",
                Object[].class).getResultList();
    }

    @Override
    public List<Object[]> ratingDistributionByCourse(Long courseId) {
        return getSession().createQuery(
                "SELECT r.rating, COUNT(r) FROM CourseRating r "
                + "WHERE r.courseId.id = :cid GROUP BY r.rating",
                Object[].class).setParameter("cid", courseId).getResultList();
    }

    @Override
    public List<Object[]> topCoursesByRating(int limit) {
        return getSession().createQuery(
                "SELECT r.courseId.title, AVG(r.rating) FROM CourseRating r "
                + "GROUP BY r.courseId.title ORDER BY AVG(r.rating) DESC",
                Object[].class).setMaxResults(limit).getResultList();
    }

    // ===== Progress =====
    @Override
    public List<Object[]> completionRateByCourse() {
        return getSession().createQuery(
                "SELECT e.courseId.title, "
                + "SUM(CASE WHEN p.completed = true THEN 1 ELSE 0 END)*1.0/COUNT(p) "
                + "FROM Progress p JOIN p.enrollmentId e GROUP BY e.courseId.title",
                Object[].class).getResultList();
    }

    @Override
    public List<Object[]> avgCompletionRateByCourse() {
        return getSession().createQuery(
                "SELECT e.courseId.title, AVG(p.lastPosition) FROM Progress p "
                + "JOIN p.enrollmentId e GROUP BY e.courseId.title",
                Object[].class).getResultList();
    }

    // ===== System Activity =====
    @Override
    public Long countNotifications() {
        return getSession().createQuery("SELECT COUNT(n) FROM Notification n", Long.class)
                .getSingleResult();
    }

    @Override
    public Long countChatMessages() {
        return getSession().createQuery("SELECT COUNT(m) FROM ChatMessage m", Long.class)
                .getSingleResult();
    }

    @Override
    public List<Object[]> countChatByCourse() {
        return getSession().createQuery(
                "SELECT ct.courseId.title, COUNT(m) FROM ChatMessage m JOIN m.threadId ct "
                + "WHERE ct.courseId IS NOT NULL GROUP BY ct.courseId.title",
                Object[].class).getResultList();
    }

    @Override
    public List<Object[]> auditLogStats() {
        return getSession().createQuery(
                "SELECT a.userId.username, a.action, COUNT(a) "
                + "FROM AuditLog a GROUP BY a.userId.username, a.action",
                Object[].class).getResultList();
    }


    @Override
    public List<Object[]> countUsersByCourse() {
        return getSession().createQuery(
                "SELECT c.title, COUNT(DISTINCT e.studentId) "
                + "FROM Enrollment e JOIN e.courseId c "
                + "GROUP BY c.title",
                Object[].class).getResultList();
    }

    @Override
    public List<Object[]> countCoursesByUser() {
        return getSession().createQuery(
                "SELECT u.username, COUNT(DISTINCT e.courseId) "
                + "FROM Enrollment e JOIN e.studentId u "
                + "GROUP BY u.username",
                Object[].class).getResultList();
    }

    @Override
    public List<Object[]> countInstructorsByCategory() {
        return getSession().createQuery(
                "SELECT c.categoryId.name, COUNT(DISTINCT c.instructorId) "
                + "FROM Course c "
                + "GROUP BY c.categoryId.name",
                Object[].class).getResultList();
    }
}
