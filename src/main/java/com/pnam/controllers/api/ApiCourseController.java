package com.pnam.controllers.api;

import com.pnam.dto.CourseRequestDTO;
import com.pnam.pojo.Category;
import com.pnam.pojo.Course;
import com.pnam.pojo.User;
import com.pnam.services.CategoryService;
import com.pnam.services.CourseService;
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
@RequestMapping("/api/courses")
@CrossOrigin
public class ApiCourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    private String generateUniqueSlug(String title) {
        String baseSlug = title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");

        String slug = baseSlug;
        int counter = 1;

        while (courseService.findBySlug(slug) != null) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        return slug;
    }

    @PreAuthorize("hasAnyAuthority('STUDENT','INSTRUCTOR')")
    @GetMapping
    public ResponseEntity<List<Course>> list(@RequestParam(required = false) Map<String, String> params, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        if ("INSTRUCTOR".equals(user.getRole())) {
            params.put("instructorId", user.getId().toString());
        }
        return ResponseEntity.ok(courseService.getCourses(params));
    }

    @PreAuthorize("hasAnyAuthority('STUDENT','INSTRUCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id) {
        Course c = courseService.getCourseById(id);
        if (c == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Course không tồn tại"));
        }
        return ResponseEntity.ok(c);
    }

    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CourseRequestDTO dto, Principal principal) {
        User instructor = userService.getUserByUsername(principal.getName());

        Category cat = categoryService.findById(dto.getCategoryId());
        if (cat == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Danh mục không tồn tại"));
        }

        String slug = generateUniqueSlug(dto.getTitle());

        Course c = new Course();
        c.setTitle(dto.getTitle());
        c.setSlug(slug);
        c.setDescription(dto.getDescription());
        c.setPrice(dto.getPrice());
        c.setCurrency(dto.getCurrency());
        c.setDurationHours(dto.getDurationHours());
        c.setStatus(dto.getStatus());
        c.setCategoryId(cat);
        c.setInstructorId(instructor);
        c.setCreatedAt(new Date());
        c.setUpdatedAt(new Date());

        courseService.saveCourse(c);
        return ResponseEntity.status(HttpStatus.CREATED).body(c);
    }

    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @Valid @RequestBody CourseRequestDTO dto,
            Principal principal) {
        Course dbCourse = courseService.getCourseById(id);
        if (dbCourse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Course không tồn tại"));
        }

        User instructor = userService.getUserByUsername(principal.getName());
        if (!dbCourse.getInstructorId().getId().equals(instructor.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Không có quyền sửa khóa học này"));
        }

        dbCourse.setTitle(dto.getTitle());
        dbCourse.setDescription(dto.getDescription());
        dbCourse.setPrice(dto.getPrice());
        dbCourse.setCurrency(dto.getCurrency());
        dbCourse.setDurationHours(dto.getDurationHours());
        dbCourse.setStatus(dto.getStatus());
        dbCourse.setUpdatedAt(new Date());

        if (dto.getCategoryId() != null) {
            Category cat = categoryService.findById(dto.getCategoryId());
            if (cat != null) {
                dbCourse.setCategoryId(cat);
            }
        }

        courseService.saveCourse(dbCourse);
        return ResponseEntity.ok(dbCourse);
    }

    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id, Principal principal) {
        Course c = courseService.getCourseById(id);
        if (c == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Course không tồn tại"));
        }

        User instructor = userService.getUserByUsername(principal.getName());
        if (!c.getInstructorId().getId().equals(instructor.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Không có quyền xóa khóa học này"));
        }

        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
