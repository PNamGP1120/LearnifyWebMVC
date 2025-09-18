package com.pnam.formatters;

import com.pnam.pojo.Category;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

public class CategoryFormatter implements Formatter<Category> {

    @Override
    public String print(Category category, Locale locale) {
        return (category != null ? String.valueOf(category.getId()) : "");
    }

    @Override
    public Category parse(String cateId, Locale locale) throws ParseException {
        if (cateId == null || cateId.isBlank()) {
            return null;
        }

        Category c = new Category();
        c.setId(Long.valueOf(cateId));
        return c;
    }
}
