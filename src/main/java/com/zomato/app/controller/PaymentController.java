package com.zomato.app.controller;

import com.zomato.app.entity.Payment;
import com.zomato.app.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping("/mock/{orderId}")
    public Payment pay(@PathVariable Long orderId) {
        return service.pay(orderId);
    }
}
