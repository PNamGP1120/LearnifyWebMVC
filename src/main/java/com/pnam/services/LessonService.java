package com.pnam.services;

import com.pnam.pojo.Lesson;
import java.util.List;

public interface LessonService {
    Lesson getLessonById(Long id);
    List<Lesson> getLessonsBySection(Long sectionId);
    Lesson createLesson(Lesson l);
    Lesson updateLesson(Lesson l);
    void deleteLesson(Long id);
}
