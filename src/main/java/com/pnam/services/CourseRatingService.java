package com.pnam.services;

import com.pnam.pojo.CourseRating;
import java.util.List;

public interface CourseRatingService {
    CourseRating getCourseRatingById(Long id);
    List<CourseRating> getRatingsByCourse(Long courseId);
    List<CourseRating> getRatingsByStudent(Long studentId);
    CourseRating getRatingByCourseAndStudent(Long courseId, Long studentId);
    CourseRating createCourseRating(CourseRating cr);
    CourseRating updateCourseRating(CourseRating cr);
    void deleteCourseRating(Long id);
}
