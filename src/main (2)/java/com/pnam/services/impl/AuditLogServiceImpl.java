package com.pnam.services.impl;

import com.pnam.pojo.AuditLog;
import com.pnam.repositories.AuditLogRepository;
import com.pnam.services.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    private AuditLogRepository logRepo;

    @Override
    public AuditLog getLogById(Long id) {
        return logRepo.findById(id);
    }

    @Override
    public List<AuditLog> getLogsByUser(Long userId) {
        return logRepo.findByUser(userId);
    }

    @Override
    public AuditLog createLog(AuditLog log) {
        return logRepo.save(log);
    }

    @Override
    public void deleteLog(Long id) {
        logRepo.delete(id);
    }

    @Override
    public List<AuditLog> getAllLogs() {
        return logRepo.findAll();
    }

}
