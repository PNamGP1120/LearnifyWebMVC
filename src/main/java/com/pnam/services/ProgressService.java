package com.pnam.services;

import com.pnam.pojo.Progress;
import java.util.List;

public interface ProgressService {
    Progress getProgressById(Long id);
    List<Progress> getProgressByEnrollment(Long enrollmentId);
    Progress createProgress(Progress p);
    Progress updateProgress(Progress p);
    void deleteProgress(Long id);
}
