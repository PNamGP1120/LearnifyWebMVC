package com.pnam.repositories;

import com.pnam.pojo.Course;
import java.util.List;
import java.util.Map;

public interface CourseRepository {
    List<Course> getCourses(Map<String, String> params);
    long countCourses(Map<String, String> params);
    Course findById(Long id);
    void save(Course course);
    void delete(Long id);
}
