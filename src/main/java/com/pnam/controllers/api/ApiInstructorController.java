package com.pnam.controllers.api;

import com.pnam.dto.InstructorProfileUpdateDTO;
import com.pnam.dto.UserProfileUpdateDTO;
import com.pnam.pojo.Course;
import com.pnam.pojo.InstructorProfile;
import com.pnam.pojo.User;
import com.pnam.services.CourseService;
import com.pnam.services.InstructorProfileService;
import com.pnam.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/instructor")
@CrossOrigin
public class ApiInstructorController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private InstructorProfileService instructorProfileService;

    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Principal principal) {
        User instructor = userService.getUserByUsername(principal.getName());
        return ResponseEntity.ok(instructor);
    }

    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @PutMapping("/profile")
    public ResponseEntity<?> updateInstructorProfile(@Valid @RequestBody UserProfileUpdateDTO dto,
            BindingResult result,
            Principal principal) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        User instructor = userService.getUserByUsername(principal.getName());
        instructor.setFullName(dto.getFullName());
        instructor.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            instructor.setPassword(dto.getPassword()); // sẽ encode trong service
        }
        if (dto.getAvatarUrl() != null && !dto.getAvatarUrl().isBlank()) {
            instructor.setAvatarUrl(dto.getAvatarUrl());
        }
        instructor.setUpdatedAt(new Date());

        userService.updateUser(instructor);
        return ResponseEntity.ok(instructor);
    }

    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','PENDING_INSTRUCTOR')")
    @GetMapping("/profile/details")
    public ResponseEntity<?> getProfileDetails(Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        InstructorProfile profile = instructorProfileService.getProfileById(user.getId());

        if (profile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Instructor profile not found"));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("user", user);
        res.put("profile", profile);

        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','PENDING_INSTRUCTOR')")
    @PutMapping("/profile/details")
    public ResponseEntity<?> updateProfileDetails(
            @Valid @RequestBody InstructorProfileUpdateDTO dto,
            Principal principal
    ) {
        User user = userService.getUserByUsername(principal.getName());
        InstructorProfile profile = instructorProfileService.getProfileById(user.getId());

        if (profile == null) {
            profile = new InstructorProfile(user.getId(), false);
        }

        profile.setBio(dto.getBio());
        profile.setCertifications(dto.getCertifications());

        instructorProfileService.updateProfile(profile);

        return ResponseEntity.ok(profile);
    }

    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getMyCourses(@RequestParam(required = false) Map<String, String> params,
            Principal principal) {
        User instructor = userService.getUserByUsername(principal.getName());
        params.put("instructorId", instructor.getId().toString());
        return ResponseEntity.ok(courseService.getCourses(params));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> listInstructors(@RequestParam(required = false) Map<String, String> params) {
        params.put("role", "INSTRUCTOR");
        return ResponseEntity.ok(userService.getUsers(params));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/lock")
    public ResponseEntity<?> lockInstructor(@PathVariable("id") Long id) {
        userService.lockUser(id);
        return ResponseEntity.ok(Map.of("message", "Đã khóa instructor có ID = " + id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/unlock")
    public ResponseEntity<?> unlockInstructor(@PathVariable("id") Long id) {
        userService.unlockUser(id);
        return ResponseEntity.ok(Map.of("message", "Đã mở khóa instructor có ID = " + id));
    }
}
