package com.zomato.app.controller;

import com.zomato.app.entity.NotificationMessage;
import com.zomato.app.service.NotificationService;
import com.zomato.app.util.AuthUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService service;
    private final AuthUtil authUtil;

    public NotificationController(NotificationService service, AuthUtil authUtil) {
        this.service = service;
        this.authUtil = authUtil;
    }

    @GetMapping
    public List<NotificationMessage> list() {
        return service.list(authUtil.currentUserEmail());
    }
}
