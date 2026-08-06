package com.zomato.app.service;

import com.zomato.app.entity.Food;
import com.zomato.app.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElasticSearchService {

    private final FoodRepository foodRepository;

    public ElasticSearchService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public List<Food> search(String query) {
        return foodRepository.findTop8ByNameContainingIgnoreCaseAndAvailableTrue(query);
    }
}
