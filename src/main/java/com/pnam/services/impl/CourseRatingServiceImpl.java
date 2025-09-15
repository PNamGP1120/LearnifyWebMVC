package com.pnam.services.impl;

import com.pnam.pojo.CourseRating;
import com.pnam.repositories.CourseRatingRepository;
import com.pnam.services.CourseRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseRatingServiceImpl implements CourseRatingService {

    @Autowired
    private CourseRatingRepository ratingRepo;

    @Override
    public CourseRating getCourseRatingById(Long id) {
        return ratingRepo.findById(id);
    }

    @Override
    public List<CourseRating> getRatingsByCourse(Long courseId) {
        return ratingRepo.findByCourse(courseId);
    }

    @Override
    public List<CourseRating> getRatingsByStudent(Long studentId) {
        return ratingRepo.findByStudent(studentId);
    }

    @Override
    public CourseRating getRatingByCourseAndStudent(Long courseId, Long studentId) {
        return ratingRepo.findByCourseAndStudent(courseId, studentId);
    }

    @Override
    public CourseRating createCourseRating(CourseRating cr) {
        return ratingRepo.save(cr);
    }

    @Override
    public CourseRating updateCourseRating(CourseRating cr) {
        return ratingRepo.save(cr);
    }

    @Override
    public void deleteCourseRating(Long id) {
        ratingRepo.delete(id);
    }
}
