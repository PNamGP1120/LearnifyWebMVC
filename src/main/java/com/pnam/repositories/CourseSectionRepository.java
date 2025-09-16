package com.pnam.repositories;

import com.pnam.pojo.CourseSection;
import java.util.List;
import java.util.Map;

public interface CourseSectionRepository {
    List<CourseSection> getSections(Map<String, String> params);
    long countSections(Map<String, String> params);
    CourseSection findById(Long id);
    void save(CourseSection section);
    void delete(Long id);
}
