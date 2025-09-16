package com.pnam.services;

import com.pnam.pojo.Course;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface CourseService {

    List<Course> getCourses(Map<String, String> params);

    long countCourses(Map<String, String> params);

    Course getCourseById(Long id);

    void saveCourse(Course course);

    void deleteCourse(Long id);

    String uploadCoverImage(MultipartFile file);
}
