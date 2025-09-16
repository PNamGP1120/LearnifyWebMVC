package com.pnam.repositories;

import com.pnam.pojo.Course;
import java.util.List;
import java.util.Map;

public interface CourseRepository {
    List<Course> getCourses(Map<String, String> params);
    long countCourses(Map<String, String> params);
    Course getCourseById(Long id);
    void saveCourse(Course course);
    void deleteCourse(Long id);
}
