package com.pnam.controllers.admin;

import com.pnam.services.StatsService;
import java.time.Year;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/stats")
public class AdminStatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping
    public String dashboard(
            @RequestParam(value = "filterType", required = false) String filterType,
            @RequestParam Map<String, String> params,
            Model model) {
        int currentYear = Year.now().getValue();
        Map<String, Object> stats = new HashMap<>();

        Map<String, String> userFilters = new HashMap<>();
        if ("users".equals(filterType) && params.containsKey("status")) {
            userFilters.put("status", params.get("status"));
            model.addAttribute("status", params.get("status"));
        }
        stats.put("usersByRole", statsService.statsUsersByRole(userFilters));

        Map<String, String> revFilters = new HashMap<>();
        String year = params.getOrDefault("year", String.valueOf(currentYear));
        revFilters.put("year", year);
        model.addAttribute("year", year);
        if ("revenue".equals(filterType) && params.containsKey("method") && !params.get("method").isEmpty()) {
            revFilters.put("method", params.get("method"));
            model.addAttribute("method", params.get("method"));
        }
        stats.put("revenueByMonth", statsService.statsRevenueByMonth(revFilters));

        Map<String, String> catFilters = new HashMap<>();
        if ("courses".equals(filterType) && params.containsKey("categoryId") && !params.get("categoryId").isEmpty()) {
            catFilters.put("categoryId", params.get("categoryId"));
            model.addAttribute("categoryId", params.get("categoryId"));
        }
        stats.put("coursesByCategory", statsService.statsCoursesByCategory(catFilters));

        stats.put("topInstructors", statsService.statsTopInstructorsByRevenue(null, 5));
        stats.put("topCourses", statsService.statsTopCoursesByEnrollments(null, 5));

        model.addAttribute("stats", stats);
        return "admin/stats";
    }
}
