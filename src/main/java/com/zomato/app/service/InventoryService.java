package com.zomato.app.service;

import com.zomato.app.entity.Food;
import com.zomato.app.entity.InventoryItem;
import com.zomato.app.exception.InvalidRequestException;
import com.zomato.app.repository.FoodRepository;
import com.zomato.app.repository.InventoryItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryItemRepository inventoryRepository;
    private final FoodRepository foodRepository;

    public InventoryService(InventoryItemRepository inventoryRepository, FoodRepository foodRepository) {
        this.inventoryRepository = inventoryRepository;
        this.foodRepository = foodRepository;
    }

    public List<InventoryItem> all() {
        return inventoryRepository.findAll();
    }

    public InventoryItem save(InventoryItem item) {
        return inventoryRepository.save(item);
    }

    @Transactional
    public void reduceStock(Long foodId, int quantity) {
        InventoryItem item = inventoryRepository.findByFoodId(foodId)
                .orElseGet(() -> {
                    Food food = foodRepository.findById(foodId)
                            .orElseThrow(() -> new InvalidRequestException("Food not found"));
                    return inventoryRepository.save(new InventoryItem(foodId, food.getName(), food.getStockQuantity()));
                });
        if (item.getStockQuantity() < quantity) {
            throw new InvalidRequestException("Food stock not available");
        }
        item.setStockQuantity(item.getStockQuantity() - quantity);
        inventoryRepository.save(item);
    }
}
