package com.pnam.repositories;

import com.pnam.pojo.InstructorProfile;
import java.util.List;
import java.util.Map;

public interface InstructorProfileRepository {

    List<InstructorProfile> getProfiles(Map<String, String> params);

    long countProfiles(Map<String, String> params);

    InstructorProfile findById(Long userId);

    void save(InstructorProfile instructor);

    void delete(Long userId);
}
