package com.pnam.controllers.admin;

import com.pnam.pojo.InstructorProfile;
import com.pnam.services.InstructorProfileService;
import com.pnam.validator.InstructorValidator;
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
@RequestMapping("/admin/instructors")
public class AdminInstructorController {

    @Autowired
    private InstructorProfileService instructorService;

    @Autowired
    private InstructorValidator instructorValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(instructorValidator);
    }

    // ================== Danh sách ==================
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

    // ================== Chi tiết ==================
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        InstructorProfile ip = instructorService.getProfileById(id);
        if (ip == null) {
            return "redirect:/admin/instructors";
        }
        model.addAttribute("instructor", ip);
        return "admin/instructor/detail";
    }

    // ================== Thêm mới ==================
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("instructor", new InstructorProfile());
        return "admin/instructor/form";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("instructor") @Valid InstructorProfile ip,
                      BindingResult result) {
        if (result.hasErrors()) {
            return "admin/instructor/form";
        }
        // dùng updateProfile() vì service không có addProfile riêng
        instructorService.updateProfile(ip);
        return "redirect:/admin/instructors";
    }

    // ================== Chỉnh sửa ==================
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        InstructorProfile ip = instructorService.getProfileById(id);
        if (ip == null) {
            return "redirect:/admin/instructors";
        }
        model.addAttribute("instructor", ip);
        return "admin/instructor/form";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id,
                       @ModelAttribute("instructor") @Valid InstructorProfile ip,
                       BindingResult result) {
        if (result.hasErrors()) {
            return "admin/instructor/form";
        }
        ip.setUserId(id); // đảm bảo update đúng
        instructorService.updateProfile(ip);
        return "redirect:/admin/instructors";
    }

    // ================== Xóa ==================
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        instructorService.deleteProfile(id);
        return "redirect:/admin/instructors";
    }
}
