package com.zomato.app.controller;

import com.zomato.app.entity.Restaurant;
import com.zomato.app.service.RestaurantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PutMapping("/{id}/timing")
    public Restaurant updateTiming(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        String openingTime = request.get("openingTime").toString();
        String closingTime = request.get("closingTime").toString();
        boolean open = Boolean.parseBoolean(request.getOrDefault("open", true).toString());
        return service.updateTiming(id, openingTime, closingTime, open);
    }
}
