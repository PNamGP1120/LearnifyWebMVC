package com.pnam.repositories;

import com.pnam.pojo.Lesson;
import java.util.List;

public interface LessonRepository {
    Lesson findById(Long id);
    List<Lesson> findBySection(Long sectionId);
    Lesson save(Lesson l);
    void delete(Long id);
}
