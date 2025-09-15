package com.pnam.repositories;

import com.pnam.pojo.Category;
import java.util.List;

public interface CategoryRepository {
    Category findById(Long id);
    Category findBySlug(String slug);
    List<Category> findAll();
    Category save(Category c);
    void delete(Long id);
}
