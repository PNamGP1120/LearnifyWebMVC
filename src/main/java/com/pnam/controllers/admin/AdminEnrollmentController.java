package com.pnam.controllers.admin;

import com.pnam.pojo.Enrollment;
import com.pnam.pojo.Payment;
import com.pnam.services.EnrollmentService;
import com.pnam.services.PaymentService;
import com.pnam.validator.EnrollmentValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/enrollments")
public class AdminEnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private EnrollmentValidator enrollmentValidator;

    @InitBinder("enrollment")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(enrollmentValidator);
    }

    @GetMapping
    public String list(@RequestParam(required = false) Map<String, String> params, Model model) {
        List<Enrollment> enrollments = enrollmentService.getEnrollments(params);
        long count = enrollmentService.countEnrollments(params);
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("count", count);
        return "admin/enrollments/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Enrollment e = enrollmentService.getEnrollmentById(id);
        if (e == null) return "redirect:/admin/enrollments";
        List<Payment> payments = paymentService.getPayments(Map.of("enrollmentId", id.toString()));
        model.addAttribute("enrollment", e);
        model.addAttribute("payments", payments);
        return "admin/enrollments/detail";
    }

    @GetMapping("/add")
    public String createForm(Model model) {
        model.addAttribute("enrollment", new Enrollment());
        return "admin/enrollments/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("enrollment") @Valid Enrollment e, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/enrollments/form";
        }
        if (e.getId() == null) {
            enrollmentService.createEnrollment(e);
        } else {
            enrollmentService.updateEnrollment(e);
        }
        return "redirect:/admin/enrollments";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        enrollmentService.deleteEnrollment(id);
        return "redirect:/admin/enrollments";
    }
}
