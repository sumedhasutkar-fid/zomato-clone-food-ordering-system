package com.zomato.app.repository;

import com.zomato.app.entity.NotificationMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationMessage, Long> {

    List<NotificationMessage> findByUserEmailOrderByCreatedAtDesc(String userEmail);
}
