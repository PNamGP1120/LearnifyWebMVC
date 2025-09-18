package com.pnam.controllers.api;

import com.pnam.pojo.CourseSection;
import com.pnam.pojo.Lesson;
import com.pnam.pojo.User;
import com.pnam.services.CourseSectionService;
import com.pnam.services.LessonService;
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
@RequestMapping("/api/lessons")
@CrossOrigin
public class ApiLessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private CourseSectionService sectionService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyAuthority('STUDENT','INSTRUCTOR')")
    @GetMapping
    public ResponseEntity<List<Lesson>> list(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(lessonService.getLessons(params));
    }

    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    @GetMapping("/by-section/{sectionId}")
    public ResponseEntity<?> listBySection(@PathVariable Long sectionId) {
        CourseSection section = sectionService.getSectionById(sectionId);
        if (section == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Section không tồn tại"));
        }
        return ResponseEntity.ok(section.getLessonSet());
    }

    @PreAuthorize("hasAnyAuthority('STUDENT','INSTRUCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id) {
        Lesson l = lessonService.getLessonById(id);
        if (l == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Lesson không tồn tại"));
        }
        return ResponseEntity.ok(l);
    }

    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Lesson lesson,
            @RequestParam("sectionId") Long sectionId,
            Principal principal) {
        User instructor = userService.getUserByUsername(principal.getName());
        CourseSection section = sectionService.getSectionById(sectionId);

        if (section == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Section không tồn tại"));
        }

        if (!section.getCourseId().getInstructorId().getId().equals(instructor.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Không có quyền thêm lesson vào section này"));
        }

        lesson.setSectionId(section);
        lessonService.saveLesson(lesson);
        return ResponseEntity.status(HttpStatus.CREATED).body(lesson);
    }

    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @Valid @RequestBody Lesson update,
            Principal principal) {
        Lesson lesson = lessonService.getLessonById(id);
        if (lesson == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Lesson không tồn tại"));
        }

        User instructor = userService.getUserByUsername(principal.getName());
        if (!lesson.getSectionId().getCourseId().getInstructorId().getId().equals(instructor.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Không có quyền sửa lesson này"));
        }

        lesson.setTitle(update.getTitle());
        lesson.setContentUrl(update.getContentUrl());
        lesson.setContentType(update.getContentType());
        lesson.setDurationMin(update.getDurationMin());
        lesson.setOrderIndex(update.getOrderIndex());
        lesson.setPreviewable(update.getPreviewable());

        lessonService.saveLesson(lesson);
        return ResponseEntity.ok(lesson);
    }

    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id, Principal principal) {
        Lesson lesson = lessonService.getLessonById(id);
        if (lesson == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Lesson không tồn tại"));
        }

        User instructor = userService.getUserByUsername(principal.getName());
        if (!lesson.getSectionId().getCourseId().getInstructorId().getId().equals(instructor.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Không có quyền xóa lesson này"));
        }

        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}
