package com.zomato.app.service;

import com.zomato.app.entity.CartItem;
import com.zomato.app.entity.FoodOrder;
import com.zomato.app.entity.OrderItem;
import com.zomato.app.exception.InvalidRequestException;
import com.zomato.app.repository.CartItemRepository;
import com.zomato.app.repository.FoodOrderRepository;
import com.zomato.app.repository.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final FoodOrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(CartItemRepository cartItemRepository,
                        FoodOrderRepository orderRepository,
                        OrderItemRepository orderItemRepository) {
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public FoodOrder placeOrder(String email, Long addressId) {
        List<CartItem> cartItems = cartItemRepository.findByUserEmail(email);

        if (cartItems.isEmpty()) {
            throw new InvalidRequestException("Cart is empty");
        }

        double total = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        FoodOrder order = orderRepository.save(new FoodOrder(email, addressId, total));

        for (CartItem item : cartItems) {
            orderItemRepository.save(new OrderItem(order.getId(), item.getFoodId(), item.getFoodName(), item.getPrice(), item.getQuantity()));
        }

        cartItemRepository.deleteByUserEmail(email);
        return order;
    }

    public List<FoodOrder> getOrders(String email) {
        return orderRepository.findByUserEmailOrderByCreatedAtDesc(email);
    }

    public FoodOrder markPaid(Long orderId) {
        FoodOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new InvalidRequestException("Order not found"));
        order.setPaymentStatus("PAID");
        return orderRepository.save(order);
    }
}
