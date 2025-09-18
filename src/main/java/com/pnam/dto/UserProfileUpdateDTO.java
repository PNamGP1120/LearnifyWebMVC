package com.pnam.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserProfileUpdateDTO {

    @NotBlank(message = "Tên đầy đủ không được để trống")
    @Size(max = 150, message = "Tên đầy đủ tối đa 150 ký tự")
    private String fullName;

    @Email(message = "Email không hợp lệ")
    @Size(max = 190, message = "Email tối đa 190 ký tự")
    private String email;

    @Size(min = 6, max = 255, message = "Mật khẩu tối thiểu 6 ký tự")
    private String password;

    private String avatarUrl;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
