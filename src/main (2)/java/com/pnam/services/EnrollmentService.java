package com.pnam.services;

import com.pnam.pojo.Enrollment;
import java.util.List;
import java.util.Map;

public interface EnrollmentService {
    List<Enrollment> getEnrollments(Map<String, String> params);
    long countEnrollments(Map<String, String> params);
    Enrollment getEnrollmentById(Long id);
    void createEnrollment(Enrollment e);
    void updateEnrollment(Enrollment e);
    void deleteEnrollment(Long id);
}
