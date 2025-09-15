package com.pnam.repositories;

import com.pnam.pojo.Enrollment;
import java.util.List;

public interface EnrollmentRepository {
    Enrollment findById(Long id);
    List<Enrollment> findByStudent(Long studentId);
    List<Enrollment> findByCourse(Long courseId);
    Enrollment findByCourseAndStudent(Long courseId, Long studentId);
    Enrollment save(Enrollment e);
    void delete(Long id);
    List<Enrollment> getAllEnrollments();
}