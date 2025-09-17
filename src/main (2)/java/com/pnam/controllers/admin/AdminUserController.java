package com.pnam.controllers.admin;

import com.pnam.pojo.User;
import com.pnam.services.UserService;
import com.pnam.validator.WebAppValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WebAppValidator userValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(userValidator);
    }

    // 📌 Danh sách user + lọc + tìm kiếm + phân trang
    @GetMapping
    public String list(@RequestParam Map<String, String> params, Model model) {
        List<User> users = userService.getUsers(params);

        // Gửi lại params ra view để giữ giá trị search/filter
        model.addAttribute("users", users);
        model.addAttribute("kw", params.get("kw"));
        model.addAttribute("role", params.get("role"));
        model.addAttribute("status", params.get("status"));

        // phân trang
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = params.containsKey("pageSize") ? Integer.parseInt(params.get("pageSize")) : 10;
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);

        return "admin/users/list";
    }

    // 📌 Chi tiết user
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/users/detail";
    }

    // 📌 Form tạo mới user
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/users/form";
    }

    // 📌 Form chỉnh sửa user
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/users/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("user") @Valid User user,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", user); // để giữ lại dữ liệu nhập
            return "admin/users/form";
        }
        if (user.getId() == null) {
            userService.register(user);
        } else {
            userService.updateUser(user);
        }
        return "redirect:/admin/users";
    }

    // 📌 Khóa user
    @GetMapping("/lock/{id}")
    public String lock(@PathVariable("id") Long id) {
        userService.lockUser(id);
        return "redirect:/admin/users";
    }

    // 📌 Mở khóa user
    @GetMapping("/unlock/{id}")
    public String unlock(@PathVariable("id") Long id) {
        userService.unlockUser(id);
        return "redirect:/admin/users";
    }

    // 📌 Xóa user
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}
