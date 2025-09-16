package com.pnam.validator;

import com.pnam.pojo.Category;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CategoryValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Category.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Category c = (Category) target;

        if (c.getName() == null || c.getName().trim().isEmpty()) {
            errors.rejectValue("name", "category.name.notBlank", "Tên danh mục không được để trống");
        } else if (c.getName().length() > 120) {
            errors.rejectValue("name", "category.name.size", "Tên danh mục không vượt quá 120 ký tự");
        }

        if (c.getSlug() == null || c.getSlug().trim().isEmpty()) {
            errors.rejectValue("slug", "category.slug.notBlank", "Slug không được để trống");
        } else if (c.getSlug().length() > 150) {
            errors.rejectValue("slug", "category.slug.size", "Slug không vượt quá 150 ký tự");
        }
    }
}
