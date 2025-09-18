package com.pnam.controllers.api;

import com.pnam.pojo.Enrollment;
import com.pnam.pojo.Progress;
import com.pnam.pojo.User;
import com.pnam.services.EnrollmentService;
import com.pnam.services.ProgressService;
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
@RequestMapping("/api/progress")
@CrossOrigin
public class ApiProgressController {

    @Autowired
    private ProgressService progressService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/my/{enrollmentId}")
    public ResponseEntity<List<Progress>> myProgress(@PathVariable("enrollmentId") Long enrollmentId,
            Principal principal) {
        User student = userService.getUserByUsername(principal.getName());
        Enrollment e = enrollmentService.getEnrollmentById(enrollmentId);
        if (e == null || !e.getStudentId().getId().equals(student.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(progressService.getProgressByEnrollment(enrollmentId));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','INSTRUCTOR')")
    @GetMapping("/enrollment/{enrollmentId}")
    public ResponseEntity<List<Progress>> getByEnrollment(@PathVariable("enrollmentId") Long enrollmentId) {
        Enrollment e = enrollmentService.getEnrollmentById(enrollmentId);
        if (e == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        return ResponseEntity.ok(progressService.getProgressByEnrollment(enrollmentId));
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @Valid @RequestBody Progress p,
            BindingResult result,
            Principal principal) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        Progress dbP = progressService.getProgressById(id);
        if (dbP == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Progress không tồn tại"));
        }

        User student = userService.getUserByUsername(principal.getName());
        if (!dbP.getEnrollmentId().getStudentId().getId().equals(student.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Không có quyền cập nhật progress này"));
        }

        dbP.setCompleted(p.getCompleted());
        dbP.setLastPosition(p.getLastPosition());
        dbP.setUpdatedAt(new Date());

        progressService.updateProgress(dbP);
        return ResponseEntity.ok(dbP);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Progress p = progressService.getProgressById(id);
        if (p == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Progress không tồn tại"));
        }
        progressService.deleteProgress(id);
        return ResponseEntity.noContent().build();
    }
}
