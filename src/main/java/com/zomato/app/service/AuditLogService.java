package com.zomato.app.service;

import com.zomato.app.entity.AuditLog;
import com.zomato.app.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository repository;

    public AuditLogService(AuditLogRepository repository) {
        this.repository = repository;
    }

    public AuditLog log(String actor, String action, String details) {
        return repository.save(new AuditLog(actor, action, details));
    }

    public List<AuditLog> all() {
        return repository.findAll();
    }
}
