package com.pnam.dto;

import jakarta.validation.constraints.*;

public class RatingRequestDTO {
    @NotNull
    @Min(1) @Max(5)
    private Short rating;

    @Size(max = 500)
    private String comment;

    @NotNull
    private Long courseId;

    // ===== Getters & Setters =====
    public Short getRating() { return rating; }
    public void setRating(Short rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
}
