package com.pnam.controllers.admin;

import com.pnam.pojo.InstructorProfile;
import com.pnam.services.InstructorProfileService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/instructors")
public class AdminInstructorController {

    @Autowired
    private InstructorProfileService instructorService;

    // Danh sách giảng viên
    @GetMapping
    public String list(Model model) {
        List<InstructorProfile> instructors = instructorService.getAllProfiles();
        model.addAttribute("instructors", instructors);
        System.out.println("c78545673486538652356242InstructorController.list()"+ instructors);
        return "admin/instructors/list";
    }

    // Chi tiết giảng viên
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        InstructorProfile instructor = instructorService.getProfileById(id);
        model.addAttribute("instructor", instructor);
        return "admin/instructors/detail";
    }

    // Trang duyệt giảng viên
    @GetMapping("/verify/{id}")
    public String verifyPage(@PathVariable("id") Long id, Model model) {
        InstructorProfile instructor = instructorService.getProfileById(id);
        model.addAttribute("instructor", instructor);
        return "admin/instructors/verify";
    }

    // Xác nhận duyệt giảng viên
    @PostMapping("/verify/{id}")
    public String verify(@PathVariable("id") Long id) {
        InstructorProfile ip = instructorService.getProfileById(id);
        if (ip != null) {
            ip.setVerifiedByAdmin(true);
            ip.setVerifiedAt(new Date());
            instructorService.updateProfile(ip);
        }
        return "redirect:/admin/instructors";
    }

    // Xóa giảng viên
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        instructorService.deleteProfile(id);
        return "redirect:/admin/instructors";
    }
}
