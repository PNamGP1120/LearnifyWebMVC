package com.pnam.controllers.api;

import com.pnam.pojo.Enrollment;
import com.pnam.pojo.Payment;
import com.pnam.pojo.User;
import com.pnam.services.EnrollmentService;
import com.pnam.services.PaymentService;
import com.pnam.services.UserService;
import com.pnam.services.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    private VnPayService vnPayService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/my")
    public ResponseEntity<List<Payment>> myPayments(Principal principal) {
        User student = userService.getUserByUsername(principal.getName());
        return ResponseEntity.ok(
                paymentService.getPayments(Map.of("studentId", student.getId().toString()))
        );
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestParam Long enrollmentId,
            Principal principal,
            HttpServletRequest req) {
        Enrollment e = enrollmentService.getEnrollmentById(enrollmentId);
        User student = userService.getUserByUsername(principal.getName());

        if (e == null || !e.getStudentId().getId().equals(student.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Enrollment không hợp lệ"));
        }

        String paymentUrl = vnPayService.createPaymentUrl(
                e.getId(),
                e.getCourseId().getPrice().longValue(),
                "Thanh toán khóa học " + e.getCourseId().getTitle(),
                req.getRemoteAddr()
        );

        return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl));
    }

    @GetMapping("/vnpay-ipn")
    public ResponseEntity<?> vnpayIpn(@RequestParam Map<String, String> params) {
        try {
            boolean valid = vnPayService.verify(params);
            if (!valid) {
                return ResponseEntity.ok(Map.of("RspCode", "97", "Message", "Invalid Checksum"));
            }

            Long enrollmentId = Long.valueOf(params.get("vnp_TxnRef"));
            Enrollment e = enrollmentService.getEnrollmentById(enrollmentId);
            if (e == null) {
                return ResponseEntity.ok(Map.of("RspCode", "01", "Message", "Order not found"));
            }

            BigDecimal amount = new BigDecimal(params.get("vnp_Amount")).divide(BigDecimal.valueOf(100));
            if (amount.compareTo(e.getCourseId().getPrice()) != 0) {
                return ResponseEntity.ok(Map.of("RspCode", "04", "Message", "Invalid Amount"));
            }
            if ("ACTIVE".equals(e.getAccessStatus())) {
                return ResponseEntity.ok(Map.of("RspCode", "02", "Message", "Order already confirmed"));
            }

            Payment p = new Payment();
            p.setEnrollmentId(e);
            p.setMethod("VNPAY");
            p.setAmount(amount);
            p.setCurrency("VND");
            p.setGatewayTxnId(params.get("vnp_TransactionNo"));
            p.setStatus("00".equals(params.get("vnp_ResponseCode")) ? "PAID" : "FAILED");
            p.setRawWebhook(params.toString());
            p.setCreatedAt(new Date());
            paymentService.savePayment(p);

            if ("00".equals(params.get("vnp_ResponseCode"))) {
                e.setAccessStatus("ACTIVE");
                enrollmentService.updateEnrollment(e);
            }

            return ResponseEntity.ok(Map.of("RspCode", "00", "Message", "Confirm Success"));
        } catch (NumberFormatException ex) {
            return ResponseEntity.ok(Map.of("RspCode", "99", "Message", "Unknown error"));
        }
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<?> vnpayReturn(@RequestParam Map<String, String> params) {
        boolean valid = vnPayService.verify(params);
        if (!valid) {
            return ResponseEntity.badRequest().body("Chữ ký VNPay không hợp lệ");
        }

        String respCode = params.get("vnp_ResponseCode");
        if ("00".equals(respCode)) {
            return ResponseEntity.ok("Giao dịch thành công!");
        } else {
            return ResponseEntity.ok("Giao dịch thất bại, mã lỗi: " + respCode);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Payment>> list(@RequestParam(required = false) Map<String, String> params) {
        return ResponseEntity.ok(paymentService.getPayments(params));
    }

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
