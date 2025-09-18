package com.pnam.dto;

import jakarta.validation.constraints.NotNull;

public class EnrollmentRequestDTO {

    @NotNull(message = "CourseId là bắt buộc")
    private Long courseId;

    private String accessStatus;

    public EnrollmentRequestDTO() {
    }

    public EnrollmentRequestDTO(Long courseId, String accessStatus) {
        this.courseId = courseId;
        this.accessStatus = accessStatus;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(String accessStatus) {
        this.accessStatus = accessStatus;
    }
}
