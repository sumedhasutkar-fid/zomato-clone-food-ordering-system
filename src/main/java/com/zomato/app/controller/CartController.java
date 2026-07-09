package com.zomato.app.controller;

import com.zomato.app.entity.CartItem;
import com.zomato.app.service.CartService;
import com.zomato.app.util.AuthUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService service;
    private final AuthUtil authUtil;

    public CartController(CartService service, AuthUtil authUtil) {
        this.service = service;
        this.authUtil = authUtil;
    }

    @GetMapping
    public List<CartItem> getCart() {
        return service.getCart(authUtil.currentUserEmail());
    }

    @PostMapping
    public CartItem addToCart(@RequestBody Map<String, Object> request) {
        Long foodId = Long.valueOf(request.get("foodId").toString());
        int quantity = Integer.parseInt(request.getOrDefault("quantity", 1).toString());
        return service.addToCart(authUtil.currentUserEmail(), foodId, quantity);
    }

    @DeleteMapping
    public void clearCart() {
        service.clearCart(authUtil.currentUserEmail());
    }
}
