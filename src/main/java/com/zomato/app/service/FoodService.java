package com.zomato.app.service;

import com.zomato.app.entity.Food;
import com.zomato.app.exception.InvalidRequestException;
import com.zomato.app.repository.FoodRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {

    private final FoodRepository repository;

    public FoodService(FoodRepository repository) {
        this.repository = repository;
    }

    public List<Food> getAllFoods() {
        return repository.findAll();
    }

    public Page<Food> searchFoods(Long restaurantId, String query, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

        if (query != null && !query.trim().isEmpty()) {
            return repository.findByRestaurantIdAndNameContainingIgnoreCaseAndAvailableTrue(restaurantId, query, pageable);
        }

        return repository.findByRestaurantIdAndAvailableTrue(restaurantId, pageable);
    }

    public Food create(Food food) {
        food.setAvailable(true);
        return repository.save(food);
    }

    public Food update(Long id, Food request) {
        Food food = repository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Food not found"));

        food.setName(request.getName());
        food.setPrice(request.getPrice());
        food.setImageUrl(request.getImageUrl());
        food.setDescription(request.getDescription());
        food.setRestaurantId(request.getRestaurantId());
        food.setCategoryId(request.getCategoryId());
        food.setAvailable(request.isAvailable());

        return repository.save(food);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
