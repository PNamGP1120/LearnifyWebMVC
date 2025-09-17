package com.pnam.validator;

import com.pnam.pojo.Lesson;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LessonValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Lesson.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Lesson l = (Lesson) target;

        if (l.getTitle() == null || l.getTitle().trim().isEmpty()) {
            errors.rejectValue("title", "lesson.title.notBlank", "Tiêu đề không được để trống");
        } else if (l.getTitle().length() > 255) {
            errors.rejectValue("title", "lesson.title.size", "Tiêu đề không vượt quá 255 ký tự");
        }

        if (l.getContentUrl() == null || l.getContentUrl().trim().isEmpty()) {
            errors.rejectValue("contentUrl", "lesson.contentUrl.notBlank", "URL nội dung không được để trống");
        } else if (l.getContentUrl().length() > 255) {
            errors.rejectValue("contentUrl", "lesson.contentUrl.size", "URL không vượt quá 255 ký tự");
        }

        if (l.getContentType() == null || l.getContentType().trim().isEmpty()) {
            errors.rejectValue("contentType", "lesson.contentType.notBlank", "Loại nội dung không được để trống");
        }
    }
}
