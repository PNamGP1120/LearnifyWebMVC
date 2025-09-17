package com.pnam.repositories;

import com.pnam.pojo.Lesson;
import java.util.List;
import java.util.Map;

public interface LessonRepository {
    List<Lesson> getLessons(Map<String, String> params);
    long countLessons(Map<String, String> params);
    Lesson findById(Long id);
    void save(Lesson lesson);
    void delete(Long id);
}
