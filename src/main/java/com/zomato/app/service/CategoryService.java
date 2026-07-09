package com.zomato.app.service;

import com.zomato.app.entity.Category;
import com.zomato.app.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> getCategories() {
        return repository.findAll();
    }

    public Category create(Category category) {
        return repository.save(category);
    }
}
