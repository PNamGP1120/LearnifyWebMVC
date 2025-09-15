package com.pnam.repositories;

import com.pnam.pojo.InstructorProfile;
import java.util.List;

public interface InstructorProfileRepository {
    List<InstructorProfile> findAll();
    InstructorProfile findById(Long userId);
    void save(InstructorProfile instructor);
    void delete(Long userId);
}
