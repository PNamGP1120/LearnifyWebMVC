
package com.pnam.validator;

import com.pnam.pojo.Enrollment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EnrollmentValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Enrollment.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Enrollment e = (Enrollment) target;

        if (e.getStudentId() == null || e.getStudentId().getId() == null) {
            errors.rejectValue("studentId", "enrollment.student.required", "Học viên bắt buộc");
        }
        if (e.getCourseId() == null || e.getCourseId().getId() == null) {
            errors.rejectValue("courseId", "enrollment.course.required", "Khoá học bắt buộc");
        }
        if (e.getAccessStatus() == null || e.getAccessStatus().isBlank()) {
            errors.rejectValue("accessStatus", "enrollment.status.required", "Trạng thái bắt buộc");
        }
    }
}
