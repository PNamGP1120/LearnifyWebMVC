package com.pnam.dto;

import java.util.Date;

public class EnrollmentResponseDTO {

    private Long id;
    private Date enrolledAt;

    private Long courseId;
    private String courseTitle;
    private String courseCategory;
    private String courseSlug;

    private Long instructorId;
    private String instructorName;
    private String instructorAvatar;

    private int progress;
    private boolean completed;

    public EnrollmentResponseDTO() {
    }

    public EnrollmentResponseDTO(Long id, Date enrolledAt,
            Long courseId, String courseTitle, String courseCategory, String courseSlug,
            Long instructorId, String instructorName, String instructorAvatar,
            int progress, boolean completed) {
        this.id = id;
        this.enrolledAt = enrolledAt;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseCategory = courseCategory;
        this.courseSlug = courseSlug;
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.instructorAvatar = instructorAvatar;
        this.progress = progress;
        this.completed = completed;
    }

    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the enrolledAt
     */
    public Date getEnrolledAt() {
        return enrolledAt;
    }

    /**
     * @param enrolledAt the enrolledAt to set
     */
    public void setEnrolledAt(Date enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    /**
     * @return the courseId
     */
    public Long getCourseId() {
        return courseId;
    }

    /**
     * @param courseId the courseId to set
     */
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    /**
     * @return the courseTitle
     */
    public String getCourseTitle() {
        return courseTitle;
    }

    /**
     * @param courseTitle the courseTitle to set
     */
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    /**
     * @return the courseCategory
     */
    public String getCourseCategory() {
        return courseCategory;
    }

    /**
     * @param courseCategory the courseCategory to set
     */
    public void setCourseCategory(String courseCategory) {
        this.courseCategory = courseCategory;
    }

    /**
     * @return the courseSlug
     */
    public String getCourseSlug() {
        return courseSlug;
    }

    /**
     * @param courseSlug the courseSlug to set
     */
    public void setCourseSlug(String courseSlug) {
        this.courseSlug = courseSlug;
    }

    /**
     * @return the instructorId
     */
    public Long getInstructorId() {
        return instructorId;
    }

    /**
     * @param instructorId the instructorId to set
     */
    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }

    /**
     * @return the instructorName
     */
    public String getInstructorName() {
        return instructorName;
    }

    /**
     * @param instructorName the instructorName to set
     */
    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    /**
     * @return the instructorAvatar
     */
    public String getInstructorAvatar() {
        return instructorAvatar;
    }

    /**
     * @param instructorAvatar the instructorAvatar to set
     */
    public void setInstructorAvatar(String instructorAvatar) {
        this.instructorAvatar = instructorAvatar;
    }

    /**
     * @return the progress
     */
    public int getProgress() {
        return progress;
    }

    /**
     * @param progress the progress to set
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    /**
     * @return the completed
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * @param completed the completed to set
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
