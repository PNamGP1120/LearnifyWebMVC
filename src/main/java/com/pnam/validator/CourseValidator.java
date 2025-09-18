package com.pnam.validator;

import com.pnam.pojo.Course;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class CourseValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Course.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Course c = (Course) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "course.title.notBlank", "Tiêu đề không được để trống");
        if (c.getTitle() != null && c.getTitle().length() > 200) {
            errors.rejectValue("title", "course.title.size", "Tiêu đề tối đa 200 ký tự");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "slug", "course.slug.notBlank", "Slug không được để trống");
        if (c.getSlug() != null && c.getSlug().length() > 220) {
            errors.rejectValue("slug", "course.slug.size", "Slug tối đa 220 ký tự");
        }

        if (c.getPrice() != null && c.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            errors.rejectValue("price", "course.price.min", "Giá phải >= 0");
        }

        if (c.getDurationHours() == null || c.getDurationHours().compareTo(BigDecimal.ZERO) <= 0) {
            errors.rejectValue("durationHours", "course.duration.positive", "Thời lượng phải > 0");
        }

        if (c.getStatus() == null || c.getStatus().isBlank()) {
            errors.rejectValue("status", "course.status.notBlank", "Trạng thái là bắt buộc");
        }

//        if (c.getCategoryId() == null || c.getCategoryId().getId() == null) {
//            errors.rejectValue("categoryId", "course.category.notNull", "Danh mục là bắt buộc");
//        }
//
//        if (c.getInstructorId() == null || c.getInstructorId().getId() == null) {
//            errors.rejectValue("instructorId", "course.instructor.notNull", "Giảng viên là bắt buộc");
//        }
    }
}
