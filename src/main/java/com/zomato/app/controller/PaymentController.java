package com.zomato.app.controller;

import com.zomato.app.entity.Payment;
import com.zomato.app.service.PaymentService;
import com.zomato.app.util.AuthUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService service;
    private final AuthUtil authUtil;

    public PaymentController(PaymentService service, AuthUtil authUtil) {
        this.service = service;
        this.authUtil = authUtil;
    }

    @PostMapping("/mock/{orderId}")
    public Payment pay(@PathVariable Long orderId) {
        return service.pay(orderId);
    }

    @PostMapping("/wallet/{orderId}")
    public Payment payWithWallet(@PathVariable Long orderId) {
        return service.payWithWallet(authUtil.currentUserEmail(), orderId);
    }
}
