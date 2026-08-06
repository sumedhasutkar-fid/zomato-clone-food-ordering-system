package com.zomato.app.service;

import com.zomato.app.entity.Restaurant;
import com.zomato.app.exception.InvalidRequestException;
import com.zomato.app.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public List<Restaurant> getAllRestaurants() {
        return repository.findAll();
    }

    public Restaurant updateTiming(Long id, String openingTime, String closingTime, boolean open) {
        Restaurant restaurant = repository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Restaurant not found"));
        restaurant.setOpeningTime(openingTime);
        restaurant.setClosingTime(closingTime);
        restaurant.setOpen(open);
        return repository.save(restaurant);
    }
}
