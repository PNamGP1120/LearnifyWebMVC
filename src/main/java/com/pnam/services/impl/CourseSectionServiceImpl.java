package com.pnam.services.impl;

import com.pnam.pojo.CourseSection;
import com.pnam.repositories.CourseSectionRepository;
import com.pnam.services.CourseSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CourseSectionServiceImpl implements CourseSectionService {

    @Autowired
    private CourseSectionRepository sectionRepo;

    @Override
    public List<CourseSection> getSections(Map<String, String> params) {
        return sectionRepo.getSections(params);
    }

    @Override
    public long countSections(Map<String, String> params) {
        return sectionRepo.countSections(params);
    }

    @Override
    public CourseSection getSectionById(Long id) {
        return sectionRepo.findById(id);
    }

    @Override
    public void saveSection(CourseSection section) {
        sectionRepo.save(section);
    }

    @Override
    public void deleteSection(Long id) {
        sectionRepo.delete(id);
    }
}
