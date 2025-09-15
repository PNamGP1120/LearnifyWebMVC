package com.pnam.repositories;

import com.pnam.pojo.CourseRating;
import java.util.List;

public interface CourseRatingRepository {
    CourseRating findById(Long id);
    List<CourseRating> findByCourse(Long courseId);
    List<CourseRating> findByStudent(Long studentId);
    CourseRating findByCourseAndStudent(Long courseId, Long studentId);
    CourseRating save(CourseRating cr);
    void delete(Long id);
}
