package com.pnam.controllers.admin;

import com.pnam.pojo.Category;
import com.pnam.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/categories/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/categories/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("category") Category c,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) return "admin/categories/form";
        categoryService.createCategory(c);
        ra.addFlashAttribute("msg", "Category created!");
        return "redirect:/admin/categories";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "admin/categories/form";
    }

    @PostMapping("/{id}/edit")
    public String edit(@Valid @ModelAttribute("category") Category c,
                       BindingResult br,
                       RedirectAttributes ra) {
        if (br.hasErrors()) return "admin/categories/form";
        categoryService.updateCategory(c);
        ra.addFlashAttribute("msg", "Category updated!");
        return "redirect:/admin/categories";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes ra) {
        categoryService.deleteCategory(id);
        ra.addFlashAttribute("msg", "Category deleted!");
        return "redirect:/admin/categories";
    }
}

