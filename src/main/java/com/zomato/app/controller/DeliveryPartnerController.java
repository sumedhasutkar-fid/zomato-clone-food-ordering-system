package com.zomato.app.controller;

import com.zomato.app.entity.DeliveryPartner;
import com.zomato.app.entity.FoodOrder;
import com.zomato.app.service.DeliveryPartnerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery")
@CrossOrigin(origins = "*")
public class DeliveryPartnerController {

    private final DeliveryPartnerService service;

    public DeliveryPartnerController(DeliveryPartnerService service) {
        this.service = service;
    }

    @GetMapping("/partners")
    public List<DeliveryPartner> partners() {
        return service.all();
    }

    @PostMapping("/{partnerId}/accept/{orderId}")
    public FoodOrder accept(@PathVariable Long partnerId, @PathVariable Long orderId) {
        return service.acceptOrder(orderId, partnerId);
    }

    @PostMapping("/pickup/{orderId}")
    public FoodOrder pickup(@PathVariable Long orderId) {
        return service.pickup(orderId);
    }

    @PostMapping("/delivered/{orderId}")
    public FoodOrder delivered(@PathVariable Long orderId) {
        return service.delivered(orderId);
    }
}
