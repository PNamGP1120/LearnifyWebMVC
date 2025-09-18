package com.pnam.repositories;

import com.pnam.pojo.Progress;
import java.util.List;

public interface ProgressRepository {

    Progress findById(Long id);

    List<Progress> findByEnrollment(Long enrollmentId);

    Progress save(Progress p);

    void delete(Long id);

    long countLessons(Long enrollmentId);

    long countCompletedLessons(Long enrollmentId);
}
