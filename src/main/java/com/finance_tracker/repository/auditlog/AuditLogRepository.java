package com.finance_tracker.repository.auditlog;

import com.finance_tracker.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    void deleteByUserId(String userId);
}
