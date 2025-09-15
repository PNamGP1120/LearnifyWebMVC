package com.pnam.services.impl;

import com.pnam.pojo.Enrollment;
import com.pnam.repositories.EnrollmentRepository;
import com.pnam.services.EnrollmentService;
import com.pnam.services.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepo;

    @Override
    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepo.findById(id);
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepo.findByStudent(studentId);
    }

    @Override
    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepo.findByCourse(courseId);
    }

    @Override
    public Enrollment getEnrollmentByCourseAndStudent(Long courseId, Long studentId) {
        return enrollmentRepo.findByCourseAndStudent(courseId, studentId);
    }

    @Override
    public Enrollment createEnrollment(Enrollment e) {
        return enrollmentRepo.save(e);
    }

    @Override
    public Enrollment updateEnrollment(Enrollment e) {
        return enrollmentRepo.save(e);
    }

    @Override
    public void deleteEnrollment(Long id) {
        enrollmentRepo.delete(id);
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepo.getAllEnrollments();
    }
}
