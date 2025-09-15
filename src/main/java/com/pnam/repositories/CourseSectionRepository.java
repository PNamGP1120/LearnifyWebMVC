package com.pnam.repositories;

import com.pnam.pojo.CourseSection;
import java.util.List;

public interface CourseSectionRepository {
    CourseSection findById(Long id);
    List<CourseSection> findByCourse(Long courseId);
    CourseSection save(CourseSection cs);
    void delete(Long id);
}
