package com.zomato.app.service;

import com.zomato.app.entity.Food;
import com.zomato.app.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    private final FoodRepository foodRepository;

    public RecommendationService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public List<Food> recommend(Long restaurantId) {
        return foodRepository.findTop6ByRestaurantIdAndAvailableTrueOrderByPriceAsc(restaurantId);
    }
}
