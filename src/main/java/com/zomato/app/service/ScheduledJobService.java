package com.zomato.app.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ScheduledJobService {

    private final AuditLogService auditLogService;
    private LocalDateTime lastRunAt;

    public ScheduledJobService(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @Scheduled(fixedDelay = 300000)
    public void runHealthJob() {
        lastRunAt = LocalDateTime.now();
        auditLogService.log("SYSTEM", "SCHEDULED_JOB", "Health job executed");
    }

    public LocalDateTime getLastRunAt() {
        return lastRunAt;
    }
}
