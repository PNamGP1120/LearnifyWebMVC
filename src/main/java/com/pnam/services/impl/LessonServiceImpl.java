package com.pnam.services.impl;

import com.pnam.pojo.Lesson;
import com.pnam.repositories.LessonRepository;
import com.pnam.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepository lessonRepo;

    @Override
    public Lesson getLessonById(Long id) {
        return lessonRepo.findById(id);
    }

    @Override
    public List<Lesson> getLessonsBySection(Long sectionId) {
        return lessonRepo.findBySection(sectionId);
    }

    @Override
    public Lesson createLesson(Lesson l) {
        return lessonRepo.save(l);
    }

    @Override
    public Lesson updateLesson(Lesson l) {
        return lessonRepo.save(l);
    }

    @Override
    public void deleteLesson(Long id) {
        lessonRepo.delete(id);
    }
}
