package com.pnam.validator;

import com.pnam.pojo.CourseRating;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CourseRatingValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CourseRating.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CourseRating r = (CourseRating) target;

        Short rating = r.getRating();
        if (rating == null) {
            errors.rejectValue("rating", "courseRating.rating.notNull", "Điểm đánh giá không được để trống");
        } else if (rating < 1 || rating > 5) {
            errors.rejectValue("rating", "courseRating.rating.range", "Điểm đánh giá phải từ 1 đến 5");
        }

        if (r.getComment() != null && r.getComment().length() > 500) {
            errors.rejectValue("comment", "courseRating.comment.size", "Nhận xét không vượt quá 500 ký tự");
        }
    }

}
