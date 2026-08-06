package com.zomato.app.service;

import com.zomato.app.entity.NotificationMessage;
import com.zomato.app.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public NotificationMessage notify(String email, String message) {
        return repository.save(new NotificationMessage(email, message));
    }

    public List<NotificationMessage> list(String email) {
        return repository.findByUserEmailOrderByCreatedAtDesc(email);
    }
}
