package com.pnam.services;

import com.pnam.pojo.InstructorProfile;
import java.util.List;
import java.util.Map;

public interface InstructorProfileService {
    List<InstructorProfile> getProfiles(Map<String, String> params);
    long countProfiles(Map<String, String> params);
    InstructorProfile getProfileById(Long userId);
    void updateProfile(InstructorProfile ip);
    void deleteProfile(Long userId);
}
