package com.pnam.services.impl;

import com.pnam.repositories.StatsRepository;
import com.pnam.services.StatsService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StatsServiceImpl implements StatsService {

    @Autowired
    private StatsRepository statsRepo;

    // ==== ADMIN ====
    @Override
    public List<Object[]> statsUsersByRole(Map<String, String> filters) {
        return statsRepo.statsUsersByRole(filters);
    }

    @Override
    public List<Object[]> statsCoursesByCategory(Map<String, String> filters) {
        return statsRepo.statsCoursesByCategory(filters);
    }

    @Override
    public List<Object[]> statsRevenueByMonth(Map<String, String> filters) {
        return statsRepo.statsRevenueByMonth(filters);
    }

    @Override
    public List<Object[]> statsTopInstructorsByRevenue(Map<String, String> filters, int limit) {
        return statsRepo.statsTopInstructorsByRevenue(filters, limit);
    }

    @Override
    public List<Object[]> statsTopCoursesByEnrollments(Map<String, String> filters, int limit) {
        return statsRepo.statsTopCoursesByEnrollments(filters, limit);
    }

    // ==== INSTRUCTOR ====
    @Override
    public List<Object[]> statsEnrollmentsByCourse(Map<String, String> filters) {
        return statsRepo.statsEnrollmentsByCourse(filters);
    }

    @Override
    public List<Object[]> statsRevenueByCourse(Map<String, String> filters) {
        return statsRepo.statsRevenueByCourse(filters);
    }

    @Override
    public List<Object[]> statsRevenueByMonthForInstructor(Map<String, String> filters) {
        return statsRepo.statsRevenueByMonthForInstructor(filters);
    }

    // ==== STUDENT ====
    @Override
    public List<Object[]> statsCoursesByStudent(Map<String, String> filters) {
        return statsRepo.statsCoursesByStudent(filters);
    }

    @Override
    public List<Object[]> statsProgressByStudent(Map<String, String> filters) {
        return statsRepo.statsProgressByStudent(filters);
    }

    @Override
    public List<Object[]> statsPaymentsByStudent(Map<String, String> filters) {
        return statsRepo.statsPaymentsByStudent(filters);
    }
}
