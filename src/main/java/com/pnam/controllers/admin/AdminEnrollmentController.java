package com.pnam.controllers.admin;

import com.pnam.pojo.Enrollment;
import com.pnam.pojo.Payment;
import com.pnam.services.EnrollmentService;
import com.pnam.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/enrollments")
public class AdminEnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private PaymentService paymentService;

    // Danh sách tất cả enrollments
    @GetMapping
    public String list(Model model) {
        // => Bạn nên implement thêm getAllEnrollments() trong service
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        model.addAttribute("enrollments", enrollments);
        return "admin/enrollments/list";
    }

    // Chi tiết 1 enrollment + các payment liên quan
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Enrollment e = enrollmentService.getEnrollmentById(id);
        if (e == null) {
            return "redirect:/admin/enrollments";
        }
        List<Payment> payments = paymentService.getPaymentsByEnrollment(e.getId());

        model.addAttribute("enrollment", e);
        model.addAttribute("payments", payments);
        return "admin/enrollments/detail";
    }

    // Form tạo mới enrollment
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("enrollment", new Enrollment());
        return "admin/enrollments/form";
    }

    // Lưu enrollment (tạo mới hoặc cập nhật)
    @PostMapping("/save")
    public String save(@ModelAttribute Enrollment e) {
        if (e.getId() == null) {
            enrollmentService.createEnrollment(e);
        } else {
            enrollmentService.updateEnrollment(e);
        }
        return "redirect:/admin/enrollments";
    }

    // Xóa enrollment
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        enrollmentService.deleteEnrollment(id);
        return "redirect:/admin/enrollments";
    }
}
