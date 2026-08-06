package com.zomato.app.service;

import com.zomato.app.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchSuggestionService {

    private final FoodRepository foodRepository;

    public SearchSuggestionService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public List<String> suggest(String query) {
        return foodRepository.findTop8ByNameContainingIgnoreCaseAndAvailableTrue(query)
                .stream()
                .map(food -> food.getName())
                .toList();
    }
}
