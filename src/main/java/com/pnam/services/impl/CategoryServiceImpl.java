package com.pnam.services.impl;

import com.pnam.pojo.Category;
import com.pnam.repositories.CategoryRepository;
import com.pnam.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repo;

    @Override
    public List<Category> getCategories(Map<String, String> params) {
        return repo.getCategories(params == null ? Collections.emptyMap() : params);
    }

    @Override
    public long countCategories(Map<String, String> params) {
        return repo.countCategories(params == null ? Collections.emptyMap() : params);
    }

    @Override
    public Category findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public void saveCategory(Category c) {
        repo.save(c);
    }

    @Override
    public void deleteCategory(Long id) {
        repo.delete(id);
    }

    @Override
    public Category getCategoryById(Long id) {
        return repo.findById(id);
    }
}
