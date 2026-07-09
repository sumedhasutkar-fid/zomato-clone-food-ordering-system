package com.zomato.app.controller;

import com.zomato.app.entity.Restaurant;
import com.zomato.app.service.RestaurantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@CrossOrigin(origins = "*")
public class RestaurantController {

    private final RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    @GetMapping
    public List<Restaurant> getRestaurants() {
        return service.getAllRestaurants();
    }
}
