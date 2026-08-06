package com.zomato.app.controller;

import com.zomato.app.service.DeliveryPartnerService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tracking")
@CrossOrigin(origins = "*")
public class TrackingController {

    private final DeliveryPartnerService service;

    public TrackingController(DeliveryPartnerService service) {
        this.service = service;
    }

    @GetMapping("/{orderId}")
    public Map<String, Object> track(@PathVariable Long orderId) {
        return service.track(orderId);
    }
}
