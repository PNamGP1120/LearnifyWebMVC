package com.pnam.services.impl;

import com.pnam.pojo.CourseSection;
import com.pnam.repositories.CourseSectionRepository;
import com.pnam.services.CourseSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseSectionServiceImpl implements CourseSectionService {

    @Autowired
    private CourseSectionRepository sectionRepo;

    @Override
    public CourseSection getCourseSectionById(Long id) {
        return sectionRepo.findById(id);
    }

    @Override
    public List<CourseSection> getSectionsByCourse(Long courseId) {
        return sectionRepo.findByCourse(courseId);
    }

    @Override
    public CourseSection createCourseSection(CourseSection cs) {
        return sectionRepo.save(cs);
    }

    @Override
    public CourseSection updateCourseSection(CourseSection cs) {
        return sectionRepo.save(cs);
    }

    @Override
    public void deleteCourseSection(Long id) {
        sectionRepo.delete(id);
    }
}
