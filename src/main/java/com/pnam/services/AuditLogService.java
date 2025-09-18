package com.pnam.services;

import com.pnam.pojo.AuditLog;
import java.util.List;

public interface AuditLogService {

    AuditLog getLogById(Long id);

    List<AuditLog> getLogsByUser(Long userId);

    AuditLog createLog(AuditLog log);

    void deleteLog(Long id);

    List<AuditLog> getAllLogs();

}
