package com.pnam.controllers.admin;

import com.pnam.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminStatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping
    public String dashboard(Model model) {
        Map<String, Object> stats = new HashMap<>();

        // --- Users ---
        stats.put("usersByRole", statsService.countUsersByRole());
        stats.put("pendingInstructors", statsService.countPendingInstructors());
        stats.put("userRegistrationsByMonth", statsService.countUserRegistrationByMonth(2025));

        // --- Courses ---
        stats.put("coursesByStatus", statsService.countCoursesByStatus());
        stats.put("coursesByCategory", statsService.countCoursesByCategory());
        stats.put("coursesByMonth", statsService.countCoursesByMonth(2025));

        // --- Enrollments ---
        stats.put("enrollmentsByCourse", statsService.countEnrollmentsByCourse());
        stats.put("enrollmentsByInstructor", statsService.countEnrollmentsByInstructor());
        stats.put("enrollmentsByMonth", statsService.countEnrollmentsByMonth(2025));
        stats.put("topCoursesByEnrollments", statsService.topCoursesByEnrollments(5));

        // --- Payments ---
        List<Object[]> revenueByMonth = statsService.revenueByMonth(2025);
        List<String> revenueMonths = new ArrayList<>();
        List<Long> revenueValues = new ArrayList<>();
        for (Object[] row : revenueByMonth) {
            revenueMonths.add(row[0].toString());   // Tháng
            revenueValues.add(((Number) row[1]).longValue()); // Doanh thu
        }
        stats.put("revenueMonths", revenueMonths);
        stats.put("revenueValues", revenueValues);

        stats.put("revenueByCourse", statsService.revenueByCourse());
        stats.put("revenueByInstructor", statsService.revenueByInstructor());
        stats.put("paymentsByStatus", statsService.countPaymentsByStatus());

        // --- Ratings ---
        stats.put("avgRatingByCourse", statsService.avgRatingByCourse());
        stats.put("topCoursesByRating", statsService.topCoursesByRating(5));

        // --- Progress ---
        stats.put("completionRateByCourse", statsService.completionRateByCourse());
        stats.put("avgCompletionRateByCourse", statsService.avgCompletionRateByCourse());

        // --- System activity ---
        stats.put("notificationsCount", statsService.countNotifications());
        stats.put("chatMessagesCount", statsService.countChatMessages());
        stats.put("chatByCourse", statsService.countChatByCourse());
        stats.put("auditLogStats", statsService.auditLogStats());

        stats.put("usersByCourse", statsService.countUsersByCourse());
        stats.put("coursesByUser", statsService.countCoursesByUser());
        stats.put("instructorsByCategory", statsService.countInstructorsByCategory());

        System.out.println("📊 Stats loaded: " + stats);

        model.addAttribute("stats", stats);
        return "admin/stats";
    }
}
