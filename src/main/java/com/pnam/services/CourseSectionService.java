package com.pnam.services;

import com.pnam.pojo.CourseSection;
import java.util.List;

public interface CourseSectionService {
    CourseSection getCourseSectionById(Long id);
    List<CourseSection> getSectionsByCourse(Long courseId);
    CourseSection createCourseSection(CourseSection cs);
    CourseSection updateCourseSection(CourseSection cs);
    void deleteCourseSection(Long id);
}
