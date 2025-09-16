package com.pnam.services;

import com.pnam.pojo.Category;
import java.util.List;
import java.util.Map;

public interface CategoryService {

    List<Category> getCategories(Map<String, String> params);

    long countCategories(Map<String, String> params);

    Category getCategoryById(Long id);

    void saveCategory(Category category);

    void deleteCategory(Long id);

    public Category findById(Long id);

}
