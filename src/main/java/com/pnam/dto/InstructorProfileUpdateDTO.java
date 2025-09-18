package com.pnam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class InstructorProfileUpdateDTO {

    @NotBlank(message = "Bio cannot be empty")
    private String bio;

    @Size(max = 500, message = "Certifications must be less than 500 characters")
    private String certifications;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCertifications() {
        return certifications;
    }

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }
}
