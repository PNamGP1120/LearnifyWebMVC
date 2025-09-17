package com.pnam.validator;

import com.pnam.pojo.CourseSection;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CourseSectionValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CourseSection.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CourseSection s = (CourseSection) target;

        if (s.getTitle() == null || s.getTitle().trim().isEmpty()) {
            errors.rejectValue("title", "courseSection.title.notBlank", "Tiêu đề mục không được để trống");
        }

        if (s.getOrderIndex() <= 0) {
            errors.rejectValue("orderIndex", "courseSection.orderIndex.min", "Thứ tự phải >= 1");
        }
    }
}
