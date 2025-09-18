package com.pnam.controllers.api;

import com.pnam.pojo.User;
import com.pnam.services.StatsService;
import com.pnam.services.UserService;
import java.security.Principal;
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

    @Autowired
    private UserService userService;

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

    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @GetMapping("/instructor/enrollments-by-course")
    public ResponseEntity<List<Object[]>> enrollmentsByCourse(
            @RequestParam Map<String, String> filters,
            Principal principal) {

        User instructor = userService.getUserByUsername(principal.getName());
        filters.put("instructorId", instructor.getId().toString());

        return ResponseEntity.ok(statsService.statsEnrollmentsByCourse(filters));
    }

    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @GetMapping("/instructor/revenue-by-course")
    public ResponseEntity<List<Object[]>> revenueByCourse(
            @RequestParam Map<String, String> filters,
            Principal principal) {

        User instructor = userService.getUserByUsername(principal.getName());
        filters.put("instructorId", instructor.getId().toString());

        return ResponseEntity.ok(statsService.statsRevenueByCourse(filters));
    }

    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @GetMapping("/instructor/revenue-by-month")
    public ResponseEntity<List<Object[]>> revenueByMonthForInstructor(
            @RequestParam Map<String, String> filters,
            Principal principal) {

        User instructor = userService.getUserByUsername(principal.getName());
        filters.put("instructorId", instructor.getId().toString());

        return ResponseEntity.ok(statsService.statsRevenueByMonthForInstructor(filters));
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/student/my-courses")
    public ResponseEntity<List<Object[]>> coursesByStudent(
            @RequestParam Map<String, String> filters,
            Principal principal) {

        User student = userService.getUserByUsername(principal.getName());
        filters.put("studentId", student.getId().toString());

        return ResponseEntity.ok(statsService.statsCoursesByStudent(filters));
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/student/my-progress")
    public ResponseEntity<List<Object[]>> progressByStudent(
            @RequestParam Map<String, String> filters,
            Principal principal) {

        User student = userService.getUserByUsername(principal.getName());
        filters.put("studentId", student.getId().toString());

        return ResponseEntity.ok(statsService.statsProgressByStudent(filters));
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/student/my-payments")
    public ResponseEntity<List<Object[]>> paymentsByStudent(
            @RequestParam Map<String, String> filters,
            Principal principal) {

        User student = userService.getUserByUsername(principal.getName());
        filters.put("studentId", student.getId().toString());

        return ResponseEntity.ok(statsService.statsPaymentsByStudent(filters));
    }

}
