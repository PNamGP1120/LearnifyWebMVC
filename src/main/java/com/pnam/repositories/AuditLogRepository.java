package com.pnam.repositories;

import com.pnam.pojo.AuditLog;
import java.util.List;

public interface AuditLogRepository {

    AuditLog findById(Long id);

    List<AuditLog> findByUser(Long userId);

    AuditLog save(AuditLog log);

    void delete(Long id);

    List<AuditLog> findAll();

}
