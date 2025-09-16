package com.pnam.validator;

import com.pnam.pojo.Course;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
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

        if (c.getTitle() == null || c.getTitle().trim().isEmpty()) {
            errors.rejectValue("title", "course.title.notBlank");
        }
        if (c.getDurationHours() == null || c.getDurationHours().doubleValue() <= 0) {
            errors.rejectValue("durationHours", "course.duration.positive");
        }
    }
}
