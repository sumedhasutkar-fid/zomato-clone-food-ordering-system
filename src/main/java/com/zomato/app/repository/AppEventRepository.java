package com.zomato.app.repository;

import com.zomato.app.entity.AppEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppEventRepository extends JpaRepository<AppEvent, Long> {
}
