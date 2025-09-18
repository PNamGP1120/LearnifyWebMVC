package com.pnam.controllers.api;

import com.pnam.pojo.Enrollment;
import com.pnam.pojo.Payment;
import com.pnam.pojo.User;
import com.pnam.services.EnrollmentService;
import com.pnam.services.PaymentService;
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
@RequestMapping("/api/payments")
@CrossOrigin
public class ApiPaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserService userService;

    // ===== GET MY PAYMENTS =====
    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/my")
    public ResponseEntity<List<Payment>> myPayments(Principal principal) {
        User student = userService.getUserByUsername(principal.getName());
        return ResponseEntity.ok(
                paymentService.getPayments(Map.of("studentId", student.getId().toString()))
        );
    }

    // ===== CREATE PAYMENT (STUDENT) =====
    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Payment p,
                                    BindingResult result,
                                    Principal principal,
                                    @RequestParam("enrollmentId") Long enrollmentId) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        User student = userService.getUserByUsername(principal.getName());
        Enrollment e = enrollmentService.getEnrollmentById(enrollmentId);
        if (e == null || !e.getStudentId().getId().equals(student.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Enrollment không hợp lệ"));
        }

        p.setEnrollmentId(e);
        p.setCreatedAt(new Date());

        paymentService.savePayment(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(p);
    }

    // ===== ADMIN: VIEW ALL PAYMENTS =====
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Payment>> list(@RequestParam(required = false) Map<String, String> params) {
        return ResponseEntity.ok(paymentService.getPayments(params));
    }

    // ===== ADMIN: DELETE PAYMENT =====
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Payment p = paymentService.getPaymentById(id);
        if (p == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Payment không tồn tại"));
        }
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
