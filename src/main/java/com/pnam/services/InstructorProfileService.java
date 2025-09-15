package com.pnam.services;

import com.pnam.pojo.InstructorProfile;
import java.util.List;

public interface InstructorProfileService {
    List<InstructorProfile> getAllProfiles();
    InstructorProfile getProfileById(Long userId);
    void updateProfile(InstructorProfile ip);
    void deleteProfile(Long userId);
}

