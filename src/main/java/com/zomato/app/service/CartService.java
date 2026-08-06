package com.zomato.app.service;

import com.zomato.app.entity.CartItem;
import com.zomato.app.entity.Food;
import com.zomato.app.exception.InvalidRequestException;
import com.zomato.app.repository.CartItemRepository;
import com.zomato.app.repository.FoodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final FoodRepository foodRepository;
    private final InventoryService inventoryService;

    public CartService(CartItemRepository cartItemRepository, FoodRepository foodRepository, InventoryService inventoryService) {
        this.cartItemRepository = cartItemRepository;
        this.foodRepository = foodRepository;
        this.inventoryService = inventoryService;
    }

    public List<CartItem> getCart(String email) {
        return cartItemRepository.findByUserEmail(email);
    }

    @Transactional
    public CartItem addToCart(String email, Long foodId, int quantity) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new InvalidRequestException("Food not found"));
        inventoryService.reduceStock(foodId, Math.max(quantity, 1));

        CartItem item = cartItemRepository.findByUserEmailAndFoodId(email, foodId)
                .orElse(new CartItem(email, food.getId(), food.getName(), food.getPrice(), 0));

        item.setQuantity(item.getQuantity() + Math.max(quantity, 1));
        return cartItemRepository.save(item);
    }

    @Transactional
    public void decreaseFromCart(String email, Long foodId) {
        CartItem item = cartItemRepository.findByUserEmailAndFoodId(email, foodId)
                .orElseThrow(() -> new InvalidRequestException("Food is not present in cart"));

        if (item.getQuantity() <= 1) {
            cartItemRepository.delete(item);
            return;
        }

        item.setQuantity(item.getQuantity() - 1);
        cartItemRepository.save(item);
    }

    @Transactional
    public void clearCart(String email) {
        cartItemRepository.deleteByUserEmail(email);
    }
}
