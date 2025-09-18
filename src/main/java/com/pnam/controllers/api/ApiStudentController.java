package com.pnam.controllers.api;

import com.pnam.dto.UserProfileUpdateDTO;
import com.pnam.pojo.User;
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
@RequestMapping("/api/student")
@CrossOrigin
public class ApiStudentController {

    @Autowired
    private UserService userService;

    // ===== GET MY PROFILE =====
    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Principal principal) {
        User student = userService.getUserByUsername(principal.getName());
        return ResponseEntity.ok(student);
    }

    // ===== UPDATE PROFILE =====
    @PreAuthorize("hasAuthority('STUDENT')")
    @PutMapping("/profile")
    public ResponseEntity<?> updateStudentProfile(@Valid @RequestBody UserProfileUpdateDTO dto,
            BindingResult result,
            Principal principal) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        User student = userService.getUserByUsername(principal.getName());
        student.setFullName(dto.getFullName());
        student.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            student.setPassword(dto.getPassword()); // sẽ encode trong service
        }
        if (dto.getAvatarUrl() != null && !dto.getAvatarUrl().isBlank()) {
            student.setAvatarUrl(dto.getAvatarUrl());
        }
        student.setUpdatedAt(new Date());

        userService.updateUser(student);
        return ResponseEntity.ok(student);
    }

    // ===== ADMIN: LIST ALL STUDENTS =====
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> listStudents(@RequestParam(required = false) Map<String, String> params) {
        params.put("role", "STUDENT");
        return ResponseEntity.ok(userService.getUsers(params));
    }
}
