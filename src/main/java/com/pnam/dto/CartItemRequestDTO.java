package com.pnam.dto;

import jakarta.validation.constraints.NotNull;

public class CartItemRequestDTO {

    @NotNull
    private Long courseId;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
