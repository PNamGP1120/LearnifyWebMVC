package com.pnam.services.impl;

import com.pnam.pojo.Enrollment;
import com.pnam.repositories.EnrollmentRepository;
import com.pnam.services.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository repo;

    @Override
    public List<Enrollment> getEnrollments(Map<String, String> params) {
        return repo.getEnrollments(params);
    }

    @Override
    public long countEnrollments(Map<String, String> params) {
        return repo.countEnrollments(params);
    }

    @Override
    public Enrollment getEnrollmentById(Long id) {
        return repo.findById(id);
    }

    @Override
    public void createEnrollment(Enrollment e) {
        repo.save(e);
    }

    @Override
    public void updateEnrollment(Enrollment e) {
        repo.save(e);
    }

    @Override
    public void deleteEnrollment(Long id) {
        repo.delete(id);
    }
}
