package com.zomato.app.controller;

import com.zomato.app.entity.Category;
import com.zomato.app.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<Category> getCategories() {
        return service.getCategories();
    }

    @PostMapping
    public Category create(@RequestBody Category category) {
        return service.create(category);
    }
}
