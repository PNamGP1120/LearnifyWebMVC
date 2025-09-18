package com.pnam.controllers.admin;

import com.pnam.pojo.InstructorProfile;
import com.pnam.pojo.User;
import com.pnam.services.InstructorProfileService;
import com.pnam.services.UserService;
import com.pnam.validator.InstructorValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/instructors")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminInstructorController {

    @Autowired
    private InstructorProfileService instructorService;

    @Autowired
    private UserService userService;

    @Autowired
    private InstructorValidator instructorValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(instructorValidator);
    }

    @GetMapping
    public String list(@RequestParam Map<String, String> params, Model model) {
        List<InstructorProfile> instructors = instructorService.getProfiles(params);
        long count = instructorService.countProfiles(params);
        int page = params.get("page") != null ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize")) : 10;
        model.addAttribute("instructors", instructors);
        model.addAttribute("count", count);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("kw", params.get("kw"));
        model.addAttribute("verified", params.get("verified"));
        return "admin/instructor/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        InstructorProfile ip = instructorService.getProfileById(id);
        if (ip == null) {
            return "redirect:/admin/instructors";
        }
        model.addAttribute("instructor", ip);
        return "admin/instructor/detail";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("instructor", new InstructorProfile());
        return "admin/instructor/create-form";
    }

    @PostMapping
    public String create(@ModelAttribute("instructor") @Valid InstructorProfile ip,
            BindingResult result) {
        if (result.hasErrors()) {
            return "admin/instructor/create-form";
        }
        instructorService.updateProfile(ip);
        return "redirect:/admin/instructors";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        InstructorProfile ip = instructorService.getProfileById(id);
        if (ip == null) {
            return "redirect:/admin/instructors";
        }
        model.addAttribute("instructor", ip);
        return "admin/instructor/edit-form";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") Long id,
            @ModelAttribute("instructor") InstructorProfile ip,
            BindingResult result) {
        if (result.hasErrors()) {
            return "admin/instructor/edit-form";
        }
        InstructorProfile existing = instructorService.getProfileById(id);
        if (ip.getBio() != null && !ip.getBio().isBlank()) {
            existing.setBio(ip.getBio());
        }
        if (ip.getCertifications() != null && !ip.getCertifications().isBlank()) {
            existing.setCertifications(ip.getCertifications());
        }
        instructorService.updateProfile(existing);
        return "redirect:/admin/instructors";
    }

    @PostMapping("/{id}/verify")
    public String verify(@PathVariable("id") Long id) {
        InstructorProfile ip = instructorService.getProfileById(id);
        if (ip == null) {
            return "redirect:/admin/instructors";
        }
        ip.setVerifiedByAdmin(true);
        ip.setVerifiedAt(new Date());
        instructorService.updateProfile(ip);
        User u = userService.getUserById(id);
        if (u != null && "PENDING_INSTRUCTOR".equals(u.getRole())) {
            u.setRole("INSTRUCTOR");
            userService.updateUser(u);
        }
        return "redirect:/admin/instructors";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        instructorService.deleteProfile(id);
        return "redirect:/admin/instructors";
    }
}
