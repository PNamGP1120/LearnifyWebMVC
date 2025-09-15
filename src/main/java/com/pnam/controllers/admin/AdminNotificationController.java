package com.pnam.controllers.admin;

import com.pnam.pojo.Notification;
import com.pnam.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/notifications")
public class AdminNotificationController {

    @Autowired
    private NotificationService notiService;

    // Danh sách tất cả thông báo
    @GetMapping
    public String list(Model model) {
        model.addAttribute("notifications", notiService.getAllNotifications());
        return "admin/notifications/list";
    }

    // Chi tiết 1 thông báo
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Notification n = notiService.getNotificationById(id);
        model.addAttribute("notification", n);
        return "admin/notifications/detail";
    }

    // Xóa thông báo
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        notiService.deleteNotification(id);
        return "redirect:/admin/notifications";
    }
}
