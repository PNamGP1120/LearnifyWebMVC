package com.pnam.repositories;

import com.pnam.pojo.User;
import java.util.List;
import java.util.Map;

public interface UserRepository {

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    void addUser(User user);

    void updateUser(User user);

    void deleteUser(Long id);

    User getUserById(Long id);

    List<User> getAllUsers();

    List<User> getUsers(Map<String, String> params);

}
