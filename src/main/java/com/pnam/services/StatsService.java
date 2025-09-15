package com.pnam.services;

import java.util.List;
import java.util.Map;

public interface StatsService {

    // Users
    Map<String, Long> countUsersByRole();

    Long countPendingInstructors();

    List<Object[]> countUserRegistrationByMonth(int year);

    // Courses
    Map<String, Long> countCoursesByStatus();

    List<Object[]> countCoursesByCategory();

    List<Object[]> countCoursesByMonth(int year);

    // Enrollments
    List<Object[]> countEnrollmentsByCourse();

    List<Object[]> countEnrollmentsByInstructor();

    List<Object[]> countEnrollmentsByMonth(int year);

    List<Object[]> topCoursesByEnrollments(int limit);

    // Payments
    List<Object[]> revenueByCourse();

    List<Object[]> revenueByInstructor();

    List<Object[]> revenueByMonth(int year);

    Map<String, Long> countPaymentsByStatus();

    // Ratings
    List<Object[]> avgRatingByCourse();

    List<Object[]> ratingDistributionByCourse(Long courseId);

    List<Object[]> topCoursesByRating(int limit);

    // Progress
    List<Object[]> completionRateByCourse();

    List<Object[]> avgCompletionRateByCourse();

    // System activity
    Long countNotifications();

    Long countChatMessages();

    List<Object[]> countChatByCourse();

    List<Object[]> auditLogStats();

    List<Object[]> countUsersByCourse();

    List<Object[]> countCoursesByUser();

    List<Object[]> countInstructorsByCategory();
}
