package com.pnam.repositories;

import com.pnam.pojo.Course;
import java.util.List;
import java.util.Map;

public interface CourseRepository {
    Course findById(Long id);
    List<Course> getCourses(Map<String, String> params);
    Course save(Course c);
    void delete(Long id);
}
