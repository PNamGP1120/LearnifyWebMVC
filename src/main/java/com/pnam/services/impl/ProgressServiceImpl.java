package com.pnam.services.impl;

import com.pnam.pojo.Progress;
import com.pnam.repositories.ProgressRepository;
import com.pnam.services.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProgressServiceImpl implements ProgressService {

    @Autowired
    private ProgressRepository progressRepo;

    @Override
    public Progress getProgressById(Long id) {
        return progressRepo.findById(id);
    }

    @Override
    public List<Progress> getProgressByEnrollment(Long enrollmentId) {
        return progressRepo.findByEnrollment(enrollmentId);
    }

    @Override
    public Progress createProgress(Progress p) {
        return progressRepo.save(p);
    }

    @Override
    public Progress updateProgress(Progress p) {
        return progressRepo.save(p);
    }

    @Override
    public void deleteProgress(Long id) {
        progressRepo.delete(id);
    }
}
