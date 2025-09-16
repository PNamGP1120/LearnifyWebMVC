package com.pnam.repositories;

import com.pnam.pojo.Category;
import java.util.List;
import java.util.Map;

public interface CategoryRepository {
    List<Category> getCategories(Map<String, String> params);
    long countCategories(Map<String, String> params);
    Category findById(Long id);
    void save(Category category);
    void delete(Long id);
}
