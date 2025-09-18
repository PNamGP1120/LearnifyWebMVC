package com.pnam.validator;

import com.pnam.pojo.User;
import com.pnam.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User u = (User) target;

        if (u.getId() == null) {
            if (userService.getUserByUsername(u.getUsername()) != null) {
                errors.rejectValue("username", "user.username.exists", "Tên tài khoản đã tồn tại");
            }
            if (userService.getUserByEmail(u.getEmail()) != null) {
                errors.rejectValue("email", "user.email.exists", "Email đã tồn tại");
            }
        }
    }
}
