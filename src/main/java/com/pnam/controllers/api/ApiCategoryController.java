package com.pnam.controllers.api;

import com.pnam.pojo.Category;
import com.pnam.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin
public class ApiCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> list(@RequestParam(required = false) Map<String, String> params) {
        return ResponseEntity.ok(categoryService.getCategories(params));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> get(@PathVariable("id") Long id) {
        Category cat = categoryService.getCategoryById(id);
        if (cat == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cat);
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category) {
        categoryService.saveCategory(category);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable("id") Long id, @RequestBody Category category) {
        Category existing = categoryService.getCategoryById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        category.setId(id);
        categoryService.saveCategory(category);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
