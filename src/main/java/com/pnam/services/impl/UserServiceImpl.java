package com.pnam.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pnam.pojo.User;
import com.pnam.repositories.UserRepository;
import com.pnam.services.UserService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

@Service("userDetailsService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public User getUserByUsername(String username) {
        return userRepo.getUserByUsername(username);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.getUserByEmail(email);
    }

    @Override
    public String uploadAvatar(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Upload avatar failed", e);
        }
    }

    @Override
    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus("ACTIVE");
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        userRepo.addUser(user);
    }

    @Override
    public void updateUser(User user) {
        User existing = userRepo.getUserById(user.getId());
        if (existing != null) {
            // Giữ nguyên các trường không thay đổi
            existing.setUsername(user.getUsername());
            existing.setFullName(user.getFullName());
            existing.setEmail(user.getEmail());
            existing.setRole(user.getRole());
            existing.setStatus(user.getStatus());

            // Nếu nhập mật khẩu mới thì encode, ngược lại giữ mật khẩu cũ
            if (user.getPassword() != null && !user.getPassword().isBlank()) {
                existing.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            // Nếu có avatar mới thì update, không thì giữ avatar cũ
            if (user.getAvatarUrl() != null && !user.getAvatarUrl().isBlank()) {
                existing.setAvatarUrl(user.getAvatarUrl());
            }

            existing.setUpdatedAt(new Date());
            userRepo.updateUser(existing);
        }
    }

    @Override
    public void lockUser(Long id) {
        User u = userRepo.getUserByUsername(getUserByUsername("username").getUsername()); // hoặc lấy theo id
        if (u != null) {
            u.setStatus("LOCKED");
            userRepo.updateUser(u);
        }
    }

    @Override
    public void unlockUser(Long id) {
        User u = userRepo.getUserByEmail(getUserByEmail("email").getEmail()); // hoặc lấy theo id
        if (u != null) {
            u.setStatus("ACTIVE");
            userRepo.updateUser(u);
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteUser(id);
    }

    // Spring Security login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = this.userRepo.getUserByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Không tìm thấy user: " + username);
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(u.getRole());
        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPassword(),
                Collections.singletonList(authority)
        );
    }

    @Override
    public User getUserById(Long id) {
        return userRepo.getUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.getAllUsers();
    }

    @Override
    public List<User> getUsers(Map<String, String> params) {
        return userRepo.getUsers(params);
    }

}
