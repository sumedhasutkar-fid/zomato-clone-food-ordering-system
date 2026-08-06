package com.zomato.app.service;

import com.zomato.app.repository.*;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodRepository foodRepository;
    private final FoodOrderRepository orderRepository;
    private final DeliveryPartnerRepository deliveryPartnerRepository;

    public AdminDashboardService(UserRepository userRepository,
                                 RestaurantRepository restaurantRepository,
                                 FoodRepository foodRepository,
                                 FoodOrderRepository orderRepository,
                                 DeliveryPartnerRepository deliveryPartnerRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.foodRepository = foodRepository;
        this.orderRepository = orderRepository;
        this.deliveryPartnerRepository = deliveryPartnerRepository;
    }

    public Map<String, Object> summary() {
        return Map.of(
                "users", userRepository.count(),
                "restaurants", restaurantRepository.count(),
                "foods", foodRepository.count(),
                "orders", orderRepository.count(),
                "deliveryPartners", deliveryPartnerRepository.count()
        );
    }
}
