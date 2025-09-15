package com.pnam.services;

import com.pnam.pojo.Enrollment;
import java.util.List;

public interface EnrollmentService {
    Enrollment getEnrollmentById(Long id);
    List<Enrollment> getEnrollmentsByStudent(Long studentId);
    List<Enrollment> getEnrollmentsByCourse(Long courseId);
    Enrollment getEnrollmentByCourseAndStudent(Long courseId, Long studentId);
    Enrollment createEnrollment(Enrollment e);
    Enrollment updateEnrollment(Enrollment e);
    void deleteEnrollment(Long id);
    List<Enrollment> getAllEnrollments();
}
