package com.pnam.controllers.api;

import com.pnam.filters.JwtUtils;
import com.pnam.pojo.User;
import com.pnam.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class ApiAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ===== REGISTER =====
    @PostMapping(path = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("fullName") String fullName,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar
    ) {
        if (userService.getUserByUsername(username) != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username đã tồn tại"));
        }
        if (userService.getUserByEmail(email) != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email đã tồn tại"));
        }

        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(password);
        u.setFullName(fullName);
        u.setRole("STUDENT"); // mặc định

        // Upload avatar nếu có
        if (avatar != null && !avatar.isEmpty()) {
            String url = userService.uploadAvatar(avatar);
            u.setAvatarUrl(url);
        }

        userService.register(u);
        return ResponseEntity.status(HttpStatus.CREATED).body(u);
    }

    // ===== LOGIN =====
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
                        .body(Map.of("error", "Lỗi khi tạo token", "details", e.getMessage()));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Sai username hoặc password"));
    }

    // ===== GET PROFILE FROM TOKEN =====
    @GetMapping("/profile")
    public ResponseEntity<?> profile(Principal principal) {
        User u = userService.getUserByUsername(principal.getName());
        if (u == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Không tìm thấy user"));
        }
        return ResponseEntity.ok(u);
    }
}
