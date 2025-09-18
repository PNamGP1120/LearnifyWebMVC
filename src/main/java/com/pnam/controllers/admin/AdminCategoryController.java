package com.pnam.controllers.admin;

import com.pnam.pojo.Category;
import com.pnam.services.CategoryService;
import com.pnam.validator.CategoryValidator;
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
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryValidator categoryValidator;

    @InitBinder("category")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(categoryValidator);
    }

    @GetMapping
    public String list(@RequestParam(required = false) Map<String, String> params, Model model) {
        if (params == null) {
            params = Map.of();
        }

        List<Category> categories = categoryService.getCategories(params);
        long count = categoryService.countCategories(params);

        String pageStr = params.getOrDefault("page", "1");
        int page = (pageStr != null && pageStr.matches("\\d+")) ? Integer.parseInt(pageStr) : 1;

        String pageSizeStr = params.getOrDefault("pageSize", "10");
        int pageSize = (pageSizeStr != null && pageSizeStr.matches("\\d+")) ? Integer.parseInt(pageSizeStr) : 10;

        model.addAttribute("categories", categories);
        model.addAttribute("count", count);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("param", params);

        return "admin/category/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category/form";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("category") @Valid Category c,
                      BindingResult result) {
        if (result.hasErrors()) {
            return "admin/category/form";
        }
        categoryService.saveCategory(c);
        return "redirect:/admin/categories";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Category c = categoryService.findById(id);
        if (c == null) {
            return "redirect:/admin/categories";
        }
        model.addAttribute("category", c);
        return "admin/category/form";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id,
                       @ModelAttribute("category") @Valid Category c,
                       BindingResult result) {
        if (result.hasErrors()) {
            return "admin/category/form";
        }
        c.setId(id);
        categoryService.saveCategory(c);
        return "redirect:/admin/categories";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }
}
