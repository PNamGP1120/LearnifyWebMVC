package com.pnam.controllers.admin;

import com.pnam.pojo.User;
import com.pnam.services.UserService;
import com.pnam.validator.WebAppValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WebAppValidator userValidator;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(userValidator);
    }

    @GetMapping
    public String list(@RequestParam Map<String, String> params, Model model) {
        List<User> users = userService.getUsers(params);
        model.addAttribute("users", users);
        model.addAttribute("kw", params.get("kw"));
        model.addAttribute("role", params.get("role"));
        model.addAttribute("status", params.get("status"));
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = params.containsKey("pageSize") ? Integer.parseInt(params.get("pageSize")) : 10;
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);
        return "admin/users/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/users/detail";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/users/create-form";
    }

    @PostMapping
    public String create(@ModelAttribute("user") @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/users/create-form";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.register(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/users/edit-form";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute("user") User user,
                         BindingResult result) {
        if (result.hasErrors()) {
            return "admin/users/edit-form";
        }
        User existing = userService.getUserById(id);
        if (user.getFullName() != null && !user.getFullName().isBlank()) {
            existing.setFullName(user.getFullName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            existing.setEmail(user.getEmail());
        }
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getRole() != null) {
            existing.setRole(user.getRole());
        }
        if (user.getStatus() != null) {
            existing.setStatus(user.getStatus());
        }
        if (user.getAvatarUrl() != null) {
            existing.setAvatarUrl(user.getAvatarUrl());
        }
        userService.updateUser(existing);
        return "redirect:/admin/users";
    }

    @GetMapping("/lock/{id}")
    public String lock(@PathVariable("id") Long id) {
        userService.lockUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/unlock/{id}")
    public String unlock(@PathVariable("id") Long id) {
        userService.unlockUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}
