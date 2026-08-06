package com.zomato.app.controller;

import com.zomato.app.entity.FoodOrder;
import com.zomato.app.service.OrderService;
import com.zomato.app.util.AuthUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService service;
    private final AuthUtil authUtil;

    public OrderController(OrderService service, AuthUtil authUtil) {
        this.service = service;
        this.authUtil = authUtil;
    }

    @GetMapping
    public List<FoodOrder> getOrders() {
        return service.getOrders(authUtil.currentUserEmail());
    }

    @PostMapping
    public FoodOrder placeOrder(@RequestBody Map<String, Object> request) {
        Long addressId = Long.valueOf(request.get("addressId").toString());
        return service.placeOrder(authUtil.currentUserEmail(), addressId);
    }

    @GetMapping("/{id}")
    public FoodOrder getOrder(@PathVariable Long id) {
        return service.getOrder(id);
    }

    @PutMapping("/{id}/status")
    public FoodOrder updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        return service.updateStatus(id, request.get("status"));
    }
}
