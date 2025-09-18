package com.pnam.repositories;

import com.pnam.pojo.Enrollment;
import java.util.List;
import java.util.Map;

public interface EnrollmentRepository {

    List<Enrollment> getEnrollments(Map<String, String> params);

    long countEnrollments(Map<String, String> params);

    Enrollment findById(Long id);

    void save(Enrollment e);

    void delete(Long id);
}
