package com.pnam.services.impl;

import com.pnam.pojo.Category;
import com.pnam.repositories.CategoryRepository;
import com.pnam.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id);
    }

    @Override
    public Category getCategoryBySlug(String slug) {
        return categoryRepo.findBySlug(slug);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Category createCategory(Category c) {
        return categoryRepo.save(c);
    }

    @Override
    public Category updateCategory(Category c) {
        return categoryRepo.save(c);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepo.delete(id);
    }
}
