package com.pnam.services.impl;

import com.pnam.pojo.InstructorProfile;
import com.pnam.repositories.InstructorProfileRepository;
import com.pnam.services.InstructorProfileService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InstructorProfileServiceImpl implements InstructorProfileService {

    @Autowired
    private InstructorProfileRepository instructorRepo;

    @Override
    public List<InstructorProfile> getAllProfiles() {
        return instructorRepo.findAll();
    }

    @Override
    public InstructorProfile getProfileById(Long userId) {
        return instructorRepo.findById(userId);
    }

    @Override
    public void updateProfile(InstructorProfile ip) {
        instructorRepo.save(ip);
    }

    @Override
    public void deleteProfile(Long userId) {
        instructorRepo.delete(userId);
    }
}

