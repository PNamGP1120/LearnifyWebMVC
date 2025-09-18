package com.pnam.controllers.api;

import com.pnam.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin
public class ApiStatsController {

    @Autowired
    private StatsService statsService;

    // ===== ADMIN =====
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/users-by-role")
    public ResponseEntity<List<Object[]>> usersByRole(@RequestParam Map<String, String> filters) {
        return ResponseEntity.ok(statsService.statsUsersByRole(filters));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/courses-by-category")
    public ResponseEntity<List<Object[]>> coursesByCategory(@RequestParam Map<String, String> filters) {
        return ResponseEntity.ok(statsService.statsCoursesByCategory(filters));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/revenue-by-month")
    public ResponseEntity<List<Object[]>> revenueByMonth(@RequestParam Map<String, String> filters) {
        return ResponseEntity.ok(statsService.statsRevenueByMonth(filters));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/top-instructors")
    public ResponseEntity<List<Object[]>> topInstructors(@RequestParam Map<String, String> filters,
                                                         @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(statsService.statsTopInstructorsByRevenue(filters, limit));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/top-courses")
    public ResponseEntity<List<Object[]>> topCourses(@RequestParam Map<String, String> filters,
                                                     @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(statsService.statsTopCoursesByEnrollments(filters, limit));
    }

    // ===== INSTRUCTOR =====
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @GetMapping("/instructor/enrollments-by-course")
    public ResponseEntity<List<Object[]>> enrollmentsByCourse(@RequestParam Map<String, String> filters) {
        return ResponseEntity.ok(statsService.statsEnrollmentsByCourse(filters));
    }

    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @GetMapping("/instructor/revenue-by-course")
    public ResponseEntity<List<Object[]>> revenueByCourse(@RequestParam Map<String, String> filters) {
        return ResponseEntity.ok(statsService.statsRevenueByCourse(filters));
    }

    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @GetMapping("/instructor/revenue-by-month")
    public ResponseEntity<List<Object[]>> revenueByMonthForInstructor(@RequestParam Map<String, String> filters) {
        return ResponseEntity.ok(statsService.statsRevenueByMonthForInstructor(filters));
    }

    // ===== STUDENT =====
    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/student/my-courses")
    public ResponseEntity<List<Object[]>> coursesByStudent(@RequestParam Map<String, String> filters) {
        return ResponseEntity.ok(statsService.statsCoursesByStudent(filters));
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/student/my-progress")
    public ResponseEntity<List<Object[]>> progressByStudent(@RequestParam Map<String, String> filters) {
        return ResponseEntity.ok(statsService.statsProgressByStudent(filters));
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/student/my-payments")
    public ResponseEntity<List<Object[]>> paymentsByStudent(@RequestParam Map<String, String> filters) {
        return ResponseEntity.ok(statsService.statsPaymentsByStudent(filters));
    }
}
