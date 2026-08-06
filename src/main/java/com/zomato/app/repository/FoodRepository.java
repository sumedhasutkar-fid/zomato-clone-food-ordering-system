package com.zomato.app.repository;


import com.zomato.app.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food,Long> {

    Page<Food> findByRestaurantIdAndAvailableTrue(Long restaurantId, Pageable pageable);

    Page<Food> findByRestaurantIdAndNameContainingIgnoreCaseAndAvailableTrue(Long restaurantId, String name, Pageable pageable);

    List<Food> findTop8ByNameContainingIgnoreCaseAndAvailableTrue(String name);

    List<Food> findTop6ByRestaurantIdAndAvailableTrueOrderByPriceAsc(Long restaurantId);

    Optional<Food> findByNameAndRestaurantId(String name, Long restaurantId);
}
