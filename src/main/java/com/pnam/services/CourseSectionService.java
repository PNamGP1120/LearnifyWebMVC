package com.pnam.services;

import com.pnam.pojo.CourseSection;
import java.util.List;
import java.util.Map;

public interface CourseSectionService {

    List<CourseSection> getSections(Map<String, String> params);

    long countSections(Map<String, String> params);

    CourseSection getSectionById(Long id);

    void saveSection(CourseSection section);

    void deleteSection(Long id);
}
