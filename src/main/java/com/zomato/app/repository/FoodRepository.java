package com.zomato.app.repository;


import com.zomato.app.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FoodRepository extends JpaRepository<Food,Long> {

    Page<Food> findByRestaurantIdAndAvailableTrue(Long restaurantId, Pageable pageable);

    Page<Food> findByRestaurantIdAndNameContainingIgnoreCaseAndAvailableTrue(Long restaurantId, String name, Pageable pageable);
}
