package com.finance_tracker.service;

import com.finance_tracker.entity.AuditLog;
import com.finance_tracker.repository.auditlog.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void saveAuditLog(AuditLog auditLog) {
        auditLogRepository.save(auditLog);
    }
}
