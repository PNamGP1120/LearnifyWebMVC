package com.pnam.controllers.api;

import com.pnam.pojo.Notification;
import com.pnam.pojo.User;
import com.pnam.services.NotificationService;
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
@RequestMapping("/api/notifications")
@CrossOrigin
public class ApiNotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    // ===== STUDENT/INSTRUCTOR: GET MY NOTIFICATIONS =====
    @PreAuthorize("hasAnyAuthority('STUDENT','INSTRUCTOR')")
    @GetMapping("/my")
    public ResponseEntity<List<Notification>> myNotifications(Principal principal) {
        User me = userService.getUserByUsername(principal.getName());
        return ResponseEntity.ok(notificationService.getNotificationsByUser(me.getId()));
    }

    // ===== STUDENT/INSTRUCTOR: MARK AS READ =====
    @PreAuthorize("hasAnyAuthority('STUDENT','INSTRUCTOR')")
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable("id") Long id, Principal principal) {
        Notification n = notificationService.getNotificationById(id);
        if (n == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Notification không tồn tại"));
        }

        User me = userService.getUserByUsername(principal.getName());
        if (!n.getUserId().getId().equals(me.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Không có quyền cập nhật notification này"));
        }

        n.setIsRead(true);
        notificationService.updateNotification(n);
        return ResponseEntity.ok(n);
    }

    // ===== ADMIN: GET ALL NOTIFICATIONS =====
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    // ===== ADMIN: SEND NOTIFICATION TO USER =====
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createNotification(@Valid @RequestBody Notification n,
                                                BindingResult result,
                                                @RequestParam("userId") Long userId) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User không tồn tại"));
        }

        n.setUserId(user);
        n.setCreatedAt(new Date());
        n.setIsRead(false);

        Notification saved = notificationService.createNotification(n);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ===== ADMIN: DELETE NOTIFICATION =====
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable("id") Long id) {
        Notification n = notificationService.getNotificationById(id);
        if (n == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Notification không tồn tại"));
        }
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
