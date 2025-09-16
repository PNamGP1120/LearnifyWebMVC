package com.pnam.services;

import com.pnam.pojo.Lesson;
import java.util.List;
import java.util.Map;

public interface LessonService {
    List<Lesson> getLessons(Map<String, String> params);
    long countLessons(Map<String, String> params);
    Lesson getLessonById(Long id);
    void saveLesson(Lesson lesson);
    void deleteLesson(Long id);
}
