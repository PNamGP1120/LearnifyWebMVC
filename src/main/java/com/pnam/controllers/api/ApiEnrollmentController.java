package com.pnam.controllers.api;

import com.pnam.dto.EnrollmentRequestDTO;
import com.pnam.pojo.Course;
import com.pnam.pojo.Enrollment;
import com.pnam.pojo.User;
import com.pnam.services.CourseService;
import com.pnam.services.EnrollmentService;
import com.pnam.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin
public class ApiEnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    // ===== STUDENT: get my enrollments =====
    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/my")
    public ResponseEntity<List<Enrollment>> myEnrollments(Principal principal) {
        User student = userService.getUserByUsername(principal.getName());
        return ResponseEntity.ok(
                enrollmentService.getEnrollments(Map.of("studentId", student.getId().toString()))
        );
    }

    // ===== STUDENT: get detail enrollment =====
    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/my/{id}")
    public ResponseEntity<?> myEnrollmentDetail(@PathVariable("id") Long id, Principal principal) {
        Enrollment e = enrollmentService.getEnrollmentById(id);
        if (e == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Enrollment không tồn tại"));
        }

        User student = userService.getUserByUsername(principal.getName());
        if (!e.getStudentId().getId().equals(student.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Không có quyền xem enrollment này"));
        }

        return ResponseEntity.ok(e);
    }

    // ===== INSTRUCTOR: get enrollments of my course =====
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> courseEnrollments(@PathVariable("courseId") Long courseId, Principal principal) {
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Course không tồn tại"));
        }

        User instructor = userService.getUserByUsername(principal.getName());
        if (!course.getInstructorId().getId().equals(instructor.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Không có quyền xem enrollments của course này"));
        }

        return ResponseEntity.ok(
                enrollmentService.getEnrollments(Map.of("courseId", courseId.toString()))
        );
    }

    // ===== STUDENT: enroll vào một course =====
    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping
    public ResponseEntity<?> enroll(@Valid @RequestBody EnrollmentRequestDTO dto, Principal principal) {
        try {
            User student = userService.getUserByUsername(principal.getName());
            Enrollment e = enrollmentService.enrollCourse(student.getId(), dto.getCourseId());
            return ResponseEntity.status(HttpStatus.CREATED).body(e);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Lỗi không xác định"));
        }
    }

}
