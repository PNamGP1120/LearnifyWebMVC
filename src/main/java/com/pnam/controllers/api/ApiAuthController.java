package com.pnam.controllers.api;

import com.pnam.filters.JwtUtils;
import com.pnam.pojo.InstructorProfile;
import com.pnam.pojo.User;
import com.pnam.services.InstructorProfileService;
import com.pnam.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class ApiAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private InstructorProfileService instructorProfileService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping(path = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(
            @RequestParam Map<String, String> params,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar
    ) {
        String username = params.get("username");
        String email = params.get("email");
        String password = params.get("password");
        String fullName = params.get("fullName");
        String role = params.getOrDefault("role", "STUDENT");

        if (userService.getUserByUsername(username) != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
        }
        if (userService.getUserByEmail(email) != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }

        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(password);
        u.setFullName(fullName);

        if ("INSTRUCTOR".equalsIgnoreCase(role)) {
            u.setRole("PENDING_INSTRUCTOR");
        } else {
            u.setRole("STUDENT");
        }

        if (avatar != null && !avatar.isEmpty()) {
            String url = userService.uploadAvatar(avatar);
            u.setAvatarUrl(url);
        }

        userService.register(u);

        if ("PENDING_INSTRUCTOR".equals(u.getRole())) {
            InstructorProfile profile = new InstructorProfile(u.getId(), false);
            instructorProfileService.updateProfile(profile); // use updateProfile() as create/update
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(u);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        User u = userService.getUserByUsername(username);
        if (u != null && passwordEncoder.matches(password, u.getPassword())) {
            try {
                String token = JwtUtils.generateToken(u.getUsername(), u.getRole());

                Map<String, Object> res = new HashMap<>();
                res.put("token", token);
                res.put("username", u.getUsername());
                res.put("role", u.getRole());

                return ResponseEntity.ok(res);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Error generating token", "details", e.getMessage()));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid username or password"));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(Principal principal) {
        User u = userService.getUserByUsername(principal.getName());
        if (u == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }
        return ResponseEntity.ok(u);
    }
}
