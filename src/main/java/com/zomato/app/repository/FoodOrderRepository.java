package com.zomato.app.repository;

import com.zomato.app.entity.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {

    List<FoodOrder> findByUserEmailOrderByCreatedAtDesc(String userEmail);
}
