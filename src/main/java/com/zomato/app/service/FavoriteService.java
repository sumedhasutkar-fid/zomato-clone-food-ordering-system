package com.zomato.app.service;

import com.zomato.app.entity.FavoriteFood;
import com.zomato.app.entity.Food;
import com.zomato.app.exception.InvalidRequestException;
import com.zomato.app.repository.FavoriteFoodRepository;
import com.zomato.app.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteFoodRepository favoriteRepository;
    private final FoodRepository foodRepository;

    public FavoriteService(FavoriteFoodRepository favoriteRepository, FoodRepository foodRepository) {
        this.favoriteRepository = favoriteRepository;
        this.foodRepository = foodRepository;
    }

    public List<FavoriteFood> list(String email) {
        return favoriteRepository.findByUserEmail(email);
    }

    public FavoriteFood toggle(String email, Long foodId) {
        return favoriteRepository.findByUserEmailAndFoodId(email, foodId)
                .map(existing -> {
                    favoriteRepository.delete(existing);
                    return existing;
                })
                .orElseGet(() -> {
                    Food food = foodRepository.findById(foodId)
                            .orElseThrow(() -> new InvalidRequestException("Food not found"));
                    return favoriteRepository.save(new FavoriteFood(email, food.getId(), food.getName()));
                });
    }
}
