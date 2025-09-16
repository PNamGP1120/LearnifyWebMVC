package com.pnam.validator;

import com.pnam.pojo.InstructorProfile;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class InstructorValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return InstructorProfile.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        InstructorProfile ip = (InstructorProfile) target;

        // Bio bắt buộc nhập và tối đa 2000 ký tự
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bio", "instructor.bio.required", "Vui lòng nhập tiểu sử");
        if (ip.getBio() != null && ip.getBio().length() > 2000) {
            errors.rejectValue("bio", "instructor.bio.size", "Tiểu sử không được vượt quá 2000 ký tự");
        }

        // Certifications tối đa 255 ký tự
        if (ip.getCertifications() != null && ip.getCertifications().length() > 255) {
            errors.rejectValue("certifications", "instructor.certifications.size", "Chứng chỉ không được vượt quá 255 ký tự");
        }

    }
}
