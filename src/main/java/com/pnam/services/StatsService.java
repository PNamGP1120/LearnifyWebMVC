package com.pnam.services;

import java.util.List;
import java.util.Map;

public interface StatsService {

    List<Object[]> statsUsersByRole(Map<String, String> filters);

    List<Object[]> statsCoursesByCategory(Map<String, String> filters);

    List<Object[]> statsRevenueByMonth(Map<String, String> filters);

    List<Object[]> statsTopInstructorsByRevenue(Map<String, String> filters, int limit);

    List<Object[]> statsTopCoursesByEnrollments(Map<String, String> filters, int limit);

    List<Object[]> statsEnrollmentsByCourse(Map<String, String> filters);

    List<Object[]> statsRevenueByCourse(Map<String, String> filters);

    List<Object[]> statsRevenueByMonthForInstructor(Map<String, String> filters);

    List<Object[]> statsCoursesByStudent(Map<String, String> filters);

    List<Object[]> statsProgressByStudent(Map<String, String> filters);

    List<Object[]> statsPaymentsByStudent(Map<String, String> filters);
}
