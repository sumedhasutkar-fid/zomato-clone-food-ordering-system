package com.zomato.app.service;

import com.zomato.app.entity.Payment;
import com.zomato.app.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    public PaymentService(PaymentRepository paymentRepository, OrderService orderService) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
    }

    public Payment pay(Long orderId) {
        orderService.markPaid(orderId);
        return paymentRepository.save(new Payment(orderId, "MOCK_RAZORPAY", "SUCCESS", "pay_" + UUID.randomUUID()));
    }
}
