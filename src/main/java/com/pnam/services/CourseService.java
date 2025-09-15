package com.pnam.services;

import com.pnam.pojo.Course;
import java.util.List;
import java.util.Map;

public interface CourseService {
    Course getCourseById(Long id);
    List<Course> getCourses(Map<String, String> params);
    Course createCourse(Course c);
    Course updateCourse(Course c);
    void deleteCourse(Long id);
}
