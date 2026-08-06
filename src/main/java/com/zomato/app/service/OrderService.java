package com.zomato.app.service;

import com.zomato.app.entity.CartItem;
import com.zomato.app.entity.FoodOrder;
import com.zomato.app.entity.OrderItem;
import com.zomato.app.exception.InvalidRequestException;
import com.zomato.app.repository.CartItemRepository;
import com.zomato.app.repository.FoodOrderRepository;
import com.zomato.app.repository.OrderItemRepository;
import com.zomato.app.util.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final FoodOrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final NotificationService notificationService;
    private final EventPublisherService eventPublisherService;
    private final AuditLogService auditLogService;

    public OrderService(CartItemRepository cartItemRepository,
                        FoodOrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        NotificationService notificationService,
                        EventPublisherService eventPublisherService,
                        AuditLogService auditLogService) {
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.notificationService = notificationService;
        this.eventPublisherService = eventPublisherService;
        this.auditLogService = auditLogService;
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
        notificationService.notify(email, "Order #" + order.getId() + " placed successfully");
        eventPublisherService.publish("ORDER_PLACED", "Order #" + order.getId() + " placed by " + email);
        auditLogService.log(email, "ORDER_PLACED", "Order amount " + total);
        return order;
    }

    public List<FoodOrder> getOrders(String email) {
        return orderRepository.findByUserEmailOrderByCreatedAtDesc(email);
    }

    public FoodOrder getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new InvalidRequestException("Order not found"));
    }

    public FoodOrder markPaid(Long orderId) {
        FoodOrder order = getOrder(orderId);
        order.setPaymentStatus("PAID");
        return orderRepository.save(order);
    }

    public FoodOrder updateStatus(Long orderId, String status) {
        if (!List.of(OrderStatus.PLACED, OrderStatus.CONFIRMED, OrderStatus.PREPARING, OrderStatus.READY, OrderStatus.OUT_FOR_DELIVERY, OrderStatus.DELIVERED).contains(status)) {
            throw new InvalidRequestException("Invalid order status");
        }

        FoodOrder order = getOrder(orderId);
        order.setStatus(status);
        order.setTrackingStatus(toTrackingStatus(status));
        notificationService.notify(order.getUserEmail(), "Order status changed to " + status);
        eventPublisherService.publish("ORDER_STATUS_CHANGED", "Order #" + orderId + " -> " + status);
        auditLogService.log("ADMIN", "ORDER_STATUS_CHANGED", "Order #" + orderId + " -> " + status);
        return orderRepository.save(order);
    }

    private String toTrackingStatus(String status) {
        return switch (status) {
            case OrderStatus.CONFIRMED -> "Restaurant confirmed your order";
            case OrderStatus.PREPARING -> "Restaurant is preparing food";
            case OrderStatus.READY -> "Food is ready for pickup";
            case OrderStatus.OUT_FOR_DELIVERY -> "Rider picked order and is on the way";
            case OrderStatus.DELIVERED -> "Delivered";
            default -> "Order placed";
        };
    }
}
