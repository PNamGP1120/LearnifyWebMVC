package com.pnam.formatters;

import com.pnam.pojo.User;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

public class UserFormatter implements Formatter<User> {

    @Override
    public String print(User user, Locale locale) {
        return (user != null ? String.valueOf(user.getId()) : "");
    }

    @Override
    public User parse(String userId, Locale locale) throws ParseException {
        if (userId == null || userId.isBlank()) {
            return null;
        }

        User u = new User();
        u.setId(Long.valueOf(userId));
        return u;
    }
}
