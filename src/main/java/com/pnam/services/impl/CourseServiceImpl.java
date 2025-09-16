package com.pnam.services.impl;

import com.pnam.pojo.Course;
import com.pnam.repositories.CourseRepository;
import com.pnam.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepo;

    @Override
    public List<Course> getCourses(Map<String, String> params) {
        return courseRepo.getCourses(params);
    }

    @Override
    public long countCourses(Map<String, String> params) {
        return courseRepo.countCourses(params);
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepo.findById(id);
    }

    @Override
    public void saveCourse(Course course) {
        courseRepo.save(course);
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepo.delete(id);
    }
}
