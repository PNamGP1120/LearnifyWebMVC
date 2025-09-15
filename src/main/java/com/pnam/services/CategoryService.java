package com.pnam.services;

import com.pnam.pojo.Category;
import java.util.List;

public interface CategoryService {
    Category getCategoryById(Long id);
    Category getCategoryBySlug(String slug);
    List<Category> getAllCategories();
    Category createCategory(Category c);
    Category updateCategory(Category c);
    void deleteCategory(Long id);
}
