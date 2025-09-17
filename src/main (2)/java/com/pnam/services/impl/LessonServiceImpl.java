package com.pnam.services.impl;

import com.pnam.pojo.Lesson;
import com.pnam.repositories.LessonRepository;
import com.pnam.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepository lessonRepo;

    @Override
    public List<Lesson> getLessons(Map<String, String> params) {
        return lessonRepo.getLessons(params);
    }

    @Override
    public long countLessons(Map<String, String> params) {
        return lessonRepo.countLessons(params);
    }

    @Override
    public Lesson getLessonById(Long id) {
        return lessonRepo.findById(id);
    }

    @Override
    public void saveLesson(Lesson lesson) {
        lessonRepo.save(lesson);
    }

    @Override
    public void deleteLesson(Long id) {
        lessonRepo.delete(id);
    }
}
