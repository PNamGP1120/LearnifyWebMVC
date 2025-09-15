package com.pnam.services;

import com.pnam.pojo.User;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends UserDetailsService {

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    void register(User user);

    void updateUser(User user);

    void lockUser(Long id);

    void unlockUser(Long id);

    void deleteUser(Long id);

    User getUserById(Long id);

    List<User> getAllUsers();

    String uploadAvatar(MultipartFile file);

    List<User> getUsers(Map<String, String> params);
}
