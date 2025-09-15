package com.pnam.services.impl;

import com.pnam.repositories.StatsRepository;
import com.pnam.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StatsServiceImpl implements StatsService {

    @Autowired
    private StatsRepository statsRepo;

    // ===== Users =====
    @Override
    public Map<String, Long> countUsersByRole() {
        return statsRepo.countUsersByRole();
    }

    @Override
    public Long countPendingInstructors() {
        return statsRepo.countPendingInstructors();
    }

    @Override
    public List<Object[]> countUserRegistrationByMonth(int year) {
        return statsRepo.countUserRegistrationByMonth(year);
    }

    // ===== Courses =====
    @Override
    public Map<String, Long> countCoursesByStatus() {
        return statsRepo.countCoursesByStatus();
    }

    @Override
    public List<Object[]> countCoursesByCategory() {
        return statsRepo.countCoursesByCategory();
    }

    @Override
    public List<Object[]> countCoursesByMonth(int year) {
        return statsRepo.countCoursesByMonth(year);
    }

    // ===== Enrollments =====
    @Override
    public List<Object[]> countEnrollmentsByCourse() {
        return statsRepo.countEnrollmentsByCourse();
    }

    @Override
    public List<Object[]> countEnrollmentsByInstructor() {
        return statsRepo.countEnrollmentsByInstructor();
    }

    @Override
    public List<Object[]> countEnrollmentsByMonth(int year) {
        return statsRepo.countEnrollmentsByMonth(year);
    }

    @Override
    public List<Object[]> topCoursesByEnrollments(int limit) {
        return statsRepo.topCoursesByEnrollments(limit);
    }

    // ===== Payments =====
    @Override
    public List<Object[]> revenueByCourse() {
        return statsRepo.revenueByCourse();
    }

    @Override
    public List<Object[]> revenueByInstructor() {
        return statsRepo.revenueByInstructor();
    }

    @Override
    public List<Object[]> revenueByMonth(int year) {
        return statsRepo.revenueByMonth(year);
    }

    @Override
    public Map<String, Long> countPaymentsByStatus() {
        return statsRepo.countPaymentsByStatus();
    }

    // ===== Ratings =====
    @Override
    public List<Object[]> avgRatingByCourse() {
        return statsRepo.avgRatingByCourse();
    }

    @Override
    public List<Object[]> ratingDistributionByCourse(Long courseId) {
        return statsRepo.ratingDistributionByCourse(courseId);
    }

    @Override
    public List<Object[]> topCoursesByRating(int limit) {
        return statsRepo.topCoursesByRating(limit);
    }

    // ===== Progress =====
    @Override
    public List<Object[]> completionRateByCourse() {
        return statsRepo.completionRateByCourse();
    }

    @Override
    public List<Object[]> avgCompletionRateByCourse() {
        return statsRepo.avgCompletionRateByCourse();
    }

    // ===== System activity =====
    @Override
    public Long countNotifications() {
        return statsRepo.countNotifications();
    }

    @Override
    public Long countChatMessages() {
        return statsRepo.countChatMessages();
    }

    @Override
    public List<Object[]> countChatByCourse() {
        return statsRepo.countChatByCourse();
    }

    @Override
    public List<Object[]> auditLogStats() {
        return statsRepo.auditLogStats();
    }

    @Override
    public List<Object[]> countUsersByCourse() {
        return statsRepo.countUsersByCourse();
    }

    @Override
    public List<Object[]> countCoursesByUser() {
        return statsRepo.countCoursesByUser();
    }

    @Override
    public List<Object[]> countInstructorsByCategory() {
        return statsRepo.countInstructorsByCategory();
    }
}
