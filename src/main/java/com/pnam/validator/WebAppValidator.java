package com.pnam.validator;

import com.pnam.pojo.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;   // ✅ import jakarta.validation.Validator
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import java.util.HashSet;
import java.util.Set;

public class WebAppValidator implements org.springframework.validation.Validator {

    @Autowired
    private Validator beanValidator;  // ✅ Spring inject LocalValidatorFactoryBean vì nó implement interface này

    private Set<org.springframework.validation.Validator> springValidators = new HashSet<>();

    public void setSpringValidators(Set<org.springframework.validation.Validator> springValidators) {
        this.springValidators = springValidators;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Set<ConstraintViolation<Object>> violations = beanValidator.validate(target);
        for (ConstraintViolation<Object> v : violations) {
            errors.rejectValue(
                v.getPropertyPath().toString(),
                v.getMessageTemplate(),
                v.getMessage()
            );
        }

        for (org.springframework.validation.Validator v : springValidators) {
            v.validate(target, errors);
        }
    }
}
