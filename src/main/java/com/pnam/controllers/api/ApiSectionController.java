package com.pnam.controllers.api;

import com.pnam.pojo.Course;
import com.pnam.pojo.CourseSection;
import com.pnam.pojo.User;
import com.pnam.services.CourseSectionService;
import com.pnam.services.CourseService;
import com.pnam.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sections")
@CrossOrigin
public class ApiSectionController {

    @Autowired
    private CourseSectionService sectionService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    // Lấy danh sách section theo course
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    @GetMapping
    public ResponseEntity<List<CourseSection>> list(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(sectionService.getSections(params));
    }

    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    @GetMapping("/by-course/{courseId}")
    public ResponseEntity<?> listByCourse(@PathVariable Long courseId) {
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Course không tồn tại"));
        }
        return ResponseEntity.ok(course.getCourseSectionSet());
    }

    // Lấy chi tiết 1 section
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id) {
        CourseSection section = sectionService.getSectionById(id);
        if (section == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Section không tồn tại"));
        }
        return ResponseEntity.ok(section);
    }

    // Tạo section mới (chỉ giảng viên)
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CourseSection section,
            @RequestParam("courseId") Long courseId,
            Principal principal) {
        User instructor = userService.getUserByUsername(principal.getName());
        Course course = courseService.getCourseById(courseId);

        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Course không tồn tại"));
        }

        // chỉ cho phép tạo section trong course của mình
        if (!course.getInstructorId().getId().equals(instructor.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Không có quyền tạo section cho course này"));
        }

        section.setCourseId(course);
        sectionService.saveSection(section);

        return ResponseEntity.status(HttpStatus.CREATED).body(section);
    }

    // Cập nhật section
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @Valid @RequestBody CourseSection update,
            Principal principal) {
        CourseSection section = sectionService.getSectionById(id);
        if (section == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Section không tồn tại"));
        }

        User instructor = userService.getUserByUsername(principal.getName());
        if (!section.getCourseId().getInstructorId().getId().equals(instructor.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Không có quyền sửa section này"));
        }

        section.setTitle(update.getTitle());
        section.setOrderIndex(update.getOrderIndex());

        sectionService.saveSection(section);
        return ResponseEntity.ok(section);
    }

    // Xóa section
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id,
            Principal principal) {
        CourseSection section = sectionService.getSectionById(id);
        if (section == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Section không tồn tại"));
        }

        User instructor = userService.getUserByUsername(principal.getName());
        if (!section.getCourseId().getInstructorId().getId().equals(instructor.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Không có quyền xóa section này"));
        }

        sectionService.deleteSection(id);
        return ResponseEntity.noContent().build();
    }
}
