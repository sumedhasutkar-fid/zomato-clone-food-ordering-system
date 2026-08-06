package com.zomato.app.service;

import com.zomato.app.entity.AppEvent;
import com.zomato.app.repository.AppEventRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventPublisherService {

    private final AppEventRepository repository;
    private final ApplicationEventPublisher publisher;

    public EventPublisherService(AppEventRepository repository, ApplicationEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    public AppEvent publish(String type, String payload) {
        AppEvent event = repository.save(new AppEvent(type, payload));
        publisher.publishEvent(event);
        return event;
    }

    public List<AppEvent> all() {
        return repository.findAll();
    }
}
