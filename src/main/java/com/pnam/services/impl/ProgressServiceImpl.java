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

    @Override
    public long countLessons(Long enrollmentId) {
        return progressRepo.countLessons(enrollmentId);
    }

    @Override
    public long countCompletedLessons(Long enrollmentId) {
        return progressRepo.countCompletedLessons(enrollmentId);
    }

    @Override
    public int calculateProgressPercent(Long enrollmentId) {
        long total = countLessons(enrollmentId);
        if (total == 0) {
            return 0;
        }

        long completed = countCompletedLessons(enrollmentId);
        return (int) Math.round((completed * 100.0) / total);
    }

    @Override
    public int calculateProgress(Long enrollmentId) {
        List<Progress> progresses = progressRepo.findByEnrollment(enrollmentId);
        if (progresses == null || progresses.isEmpty()) {
            return 0;
        }

        int totalLessons = progresses.size();
        long completedCount = progresses.stream().filter(Progress::getCompleted).count();

        return (int) ((completedCount * 100) / totalLessons);
    }
}
