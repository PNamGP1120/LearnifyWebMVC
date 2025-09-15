package com.pnam.controllers.admin;

import com.pnam.services.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/audit-logs")
public class AdminAuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    @GetMapping
    public String listAuditLogs(Model model) {
        model.addAttribute("logs", auditLogService.getAllLogs());
        return "admin/logs";
    }

    @GetMapping("/user/{userId}")
    public String logsByUser(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("logs", auditLogService.getLogsByUser(userId));
        return "admin/logs";
    }
}
