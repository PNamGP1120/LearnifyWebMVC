package com.pnam.controllers.api;

import com.pnam.pojo.Course;
import com.pnam.pojo.CourseRating;
import com.pnam.pojo.User;
import com.pnam.services.CourseRatingService;
import com.pnam.services.CourseService;
import com.pnam.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin
public class ApiRatingController {

    @Autowired
    private CourseRatingService ratingService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    // ===== GET RATINGS BY COURSE =====
    @PreAuthorize("hasAnyAuthority('STUDENT','INSTRUCTOR')")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseRating>> getRatingsByCourse(@PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(ratingService.getRatings(Map.of("courseId", courseId.toString())));
    }

    // ===== CREATE RATING (STUDENT) =====
    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CourseRating r,
                                    BindingResult result,
                                    Principal principal,
                                    @RequestParam("courseId") Long courseId) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        User student = userService.getUserByUsername(principal.getName());
        Course c = courseService.getCourseById(courseId);
        if (c == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Course không tồn tại"));
        }

        r.setStudentId(student);
        r.setCourseId(c);
        r.setCreatedAt(new Date());

        ratingService.saveRating(r);
        return ResponseEntity.status(HttpStatus.CREATED).body(r);
    }

    // ===== DELETE RATING =====
    @PreAuthorize("hasAuthority('STUDENT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id, Principal principal) {
        CourseRating r = ratingService.getRatingById(id);
        if (r == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Rating không tồn tại"));
        }

        User student = userService.getUserByUsername(principal.getName());
        if (!r.getStudentId().getId().equals(student.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Không có quyền xóa rating này"));
        }

        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build();
    }
}
