package com.zomato.app.controller;

import com.zomato.app.entity.AuditLog;
import com.zomato.app.entity.AppEvent;
import com.zomato.app.service.AdminDashboardService;
import com.zomato.app.service.AuditLogService;
import com.zomato.app.service.EventPublisherService;
import com.zomato.app.service.ScheduledJobService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminDashboardService dashboardService;
    private final AuditLogService auditLogService;
    private final EventPublisherService eventPublisherService;
    private final ScheduledJobService scheduledJobService;

    public AdminController(AdminDashboardService dashboardService,
                           AuditLogService auditLogService,
                           EventPublisherService eventPublisherService,
                           ScheduledJobService scheduledJobService) {
        this.dashboardService = dashboardService;
        this.auditLogService = auditLogService;
        this.eventPublisherService = eventPublisherService;
        this.scheduledJobService = scheduledJobService;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return dashboardService.summary();
    }

    @GetMapping("/audit-logs")
    public List<AuditLog> auditLogs() {
        return auditLogService.all();
    }

    @GetMapping("/events")
    public List<AppEvent> events() {
        return eventPublisherService.all();
    }

    @PostMapping("/events")
    public AppEvent publish(@RequestBody Map<String, String> request) {
        return eventPublisherService.publish(request.get("type"), request.get("payload"));
    }

    @GetMapping("/jobs")
    public Map<String, Object> jobs() {
        LocalDateTime lastRunAt = scheduledJobService.getLastRunAt();
        return Map.of("lastRunAt", lastRunAt == null ? "Not executed yet" : lastRunAt);
    }
}
