package com.zomato.app.controller;

import com.zomato.app.entity.Food;
import com.zomato.app.service.FoodService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foods")
@CrossOrigin(origins = "*")
public class FoodController {

    private final FoodService service;

    public FoodController(FoodService service) {
        this.service = service;
    }

    @GetMapping
    public List<Food> getFoods() {
        return service.getAllFoods();
    }

    @GetMapping("/search")
    public Page<Food> searchFoods(@RequestParam(defaultValue = "1") Long restaurantId,
                                  @RequestParam(defaultValue = "") String query,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "12") int size,
                                  @RequestParam(defaultValue = "name") String sortBy) {
        return service.searchFoods(restaurantId, query, page, size, sortBy);
    }

    @PostMapping
    public Food create(@RequestBody Food food) {
        return service.create(food);
    }

    @PutMapping("/{id}")
    public Food update(@PathVariable Long id, @RequestBody Food food) {
        return service.update(id, food);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
