package com.pnam.controllers.admin;

import com.pnam.pojo.Enrollment;
import com.pnam.pojo.Payment;
import com.pnam.services.CourseService;
import com.pnam.services.EnrollmentService;
import com.pnam.services.PaymentService;
import com.pnam.services.UserService;
import com.pnam.validator.EnrollmentValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin/enrollments")
public class AdminEnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;

    @Autowired
    private EnrollmentValidator enrollmentValidator;

    @InitBinder("enrollment")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(enrollmentValidator);
    }

// ==================== Danh sách Enrollment ====================
    @GetMapping
    public String list(@RequestParam(required = false) Map<String, String> params, Model model) {
        if (params == null) {
            params = new HashMap<>();
        }

        // Lấy danh sách enrollment theo params
        List<Enrollment> enrollments = enrollmentService.getEnrollments(params);
        long count = enrollmentService.countEnrollments(params);

        // Phân trang
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = params.containsKey("pageSize") ? Integer.parseInt(params.get("pageSize")) : 10;

        // Đếm số lượng payment cho từng enrollment (fix lỗi không render)
        Map<Long, Integer> paymentCounts = new HashMap<>();
        for (Enrollment e : enrollments) {
            int size = (e.getPaymentSet() != null) ? e.getPaymentSet().size() : 0;
            paymentCounts.put(e.getId(), size);
        }

        // Add attributes cho view
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("paymentCounts", paymentCounts);
        model.addAttribute("count", count);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("param", params);

        // filter data
        model.addAttribute("courses", courseService.getCourses(Map.of()));
        model.addAttribute("students", userService.getUsers(Map.of("role", "STUDENT")));

        return "admin/enrollments/list";
    }

    // ==================== Chi tiết Enrollment ====================
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Enrollment e = enrollmentService.getEnrollmentById(id);
        if (e == null) {
            return "redirect:/admin/enrollments";
        }

        List<Payment> payments = paymentService.getPayments(Map.of("enrollmentId", id.toString()));

        model.addAttribute("enrollment", e);
        model.addAttribute("payments", payments);
        return "admin/enrollments/detail";
    }

    // ==================== Form thêm ====================
    @GetMapping("/add")
    public String createForm(Model model) {
        model.addAttribute("enrollment", new Enrollment());
        model.addAttribute("students", userService.getUsers(Map.of("role", "STUDENT")));
        model.addAttribute("courses", courseService.getAllCourses());

        return "admin/enrollments/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Enrollment e = enrollmentService.getEnrollmentById(id);
        if (e == null) {
            return "redirect:/admin/enrollments";
        }

        model.addAttribute("enrollment", e);
        model.addAttribute("students", userService.getUsers(Map.of("role", "STUDENT")));
        model.addAttribute("courses", courseService.getAllCourses());

        return "admin/enrollments/form";
    }

    // ==================== Save ====================
    @PostMapping("/save")
    public String save(@ModelAttribute("enrollment") @Valid Enrollment e,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("courses", courseService.getCourses(Map.of()));
            model.addAttribute("students", userService.getUsers(Map.of("role", "STUDENT")));
            return "admin/enrollments/form";
        }

        if (e.getId() == null) {
            enrollmentService.createEnrollment(e);
        } else {
            enrollmentService.updateEnrollment(e);
        }
        return "redirect:/admin/enrollments";
    }

    // ==================== Xoá ====================
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        enrollmentService.deleteEnrollment(id);
        return "redirect:/admin/enrollments";
    }
}
