package com.zomato.app.service;

import com.zomato.app.entity.DeliveryPartner;
import com.zomato.app.entity.FoodOrder;
import com.zomato.app.exception.InvalidRequestException;
import com.zomato.app.repository.DeliveryPartnerRepository;
import com.zomato.app.repository.FoodOrderRepository;
import com.zomato.app.util.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class DeliveryPartnerService {

    private final DeliveryPartnerRepository deliveryPartnerRepository;
    private final FoodOrderRepository orderRepository;
    private final NotificationService notificationService;

    public DeliveryPartnerService(DeliveryPartnerRepository deliveryPartnerRepository,
                                  FoodOrderRepository orderRepository,
                                  NotificationService notificationService) {
        this.deliveryPartnerRepository = deliveryPartnerRepository;
        this.orderRepository = orderRepository;
        this.notificationService = notificationService;
    }

    public List<DeliveryPartner> all() {
        return deliveryPartnerRepository.findAll();
    }

    @Transactional
    public FoodOrder acceptOrder(Long orderId, Long partnerId) {
        FoodOrder order = getOrder(orderId);
        DeliveryPartner partner = deliveryPartnerRepository.findById(partnerId)
                .orElseThrow(() -> new InvalidRequestException("Delivery partner not found"));
        partner.setAvailable(false);
        deliveryPartnerRepository.save(partner);
        order.setDeliveryPartnerId(partner.getId());
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTrackingStatus("Delivery partner accepted order");
        notificationService.notify(order.getUserEmail(), "Delivery partner accepted your order");
        return orderRepository.save(order);
    }

    @Transactional
    public FoodOrder pickup(Long orderId) {
        FoodOrder order = getOrder(orderId);
        order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
        order.setTrackingStatus("Rider picked your order");
        order.setDistanceKm(3.2);
        notificationService.notify(order.getUserEmail(), "Your order is out for delivery");
        return orderRepository.save(order);
    }

    @Transactional
    public FoodOrder delivered(Long orderId) {
        FoodOrder order = getOrder(orderId);
        order.setStatus(OrderStatus.DELIVERED);
        order.setTrackingStatus("Delivered");
        order.setDistanceKm(0);
        if (order.getDeliveryPartnerId() != null) {
            deliveryPartnerRepository.findById(order.getDeliveryPartnerId()).ifPresent(partner -> {
                partner.setAvailable(true);
                partner.setCurrentDistanceKm(0);
                deliveryPartnerRepository.save(partner);
            });
        }
        notificationService.notify(order.getUserEmail(), "Order delivered successfully");
        return orderRepository.save(order);
    }

    public Map<String, Object> track(Long orderId) {
        FoodOrder order = getOrder(orderId);
        return Map.of(
                "orderId", order.getId(),
                "status", order.getStatus(),
                "trackingStatus", order.getTrackingStatus(),
                "distanceKm", order.getDistanceKm(),
                "deliveryPartnerId", order.getDeliveryPartnerId() == null ? 0 : order.getDeliveryPartnerId()
        );
    }

    private FoodOrder getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new InvalidRequestException("Order not found"));
    }
}
