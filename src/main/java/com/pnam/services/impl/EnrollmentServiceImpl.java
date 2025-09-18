package com.pnam.services.impl;

import com.pnam.pojo.Course;
import com.pnam.pojo.Enrollment;
import com.pnam.pojo.User;
import com.pnam.repositories.CourseRepository;
import com.pnam.repositories.EnrollmentRepository;
import com.pnam.repositories.UserRepository;
import com.pnam.services.EnrollmentService;
import java.util.Date;
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
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CourseRepository courseRepo;

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

    @Override
    public Enrollment enrollCourse(Long studentId, Long courseId) {
        User student = userRepo.getUserById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student không tồn tại");
        }

        Course course = courseRepo.getCourseById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course không tồn tại");
        }
        
        Map<String, String> params = Map.of(
                "studentId", studentId.toString(),
                "courseId", courseId.toString()
        );
        if (!repo.getEnrollments(params).isEmpty()) {
            throw new IllegalStateException("Student đã enroll course này rồi");
        }

        Enrollment e = new Enrollment();
        e.setStudentId(student);
        e.setCourseId(course);
        e.setEnrolledAt(new Date());
        e.setAccessStatus("ACTIVE");

        repo.save(e);
        return e;
    }

}
