package com.pnam.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pnam.pojo.Course;
import com.pnam.repositories.CourseRepository;
import com.pnam.services.CourseService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository repo;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<Course> getCourses(Map<String, String> params) {
        return repo.getCourses(params);
    }

    @Override
    public long countCourses(Map<String, String> params) {
        return repo.countCourses(params);
    }

    @Override
    public Course getCourseById(Long id) {
        return repo.getCourseById(id);
    }

    @Override
    public void saveCourse(Course c) {
        repo.saveCourse(c);
    }

    @Override
    public void deleteCourse(Long id) {
        repo.deleteCourse(id);
    }

    @Override
    public String uploadCoverImage(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Upload cover image failed", e);
        }
    }

    @Override
    public Course findBySlug(String slug) {
        return repo.findBySlug(slug);
    }

}
