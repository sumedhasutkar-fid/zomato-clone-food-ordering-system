package com.zomato.app.service;

import com.zomato.app.entity.Payment;
import com.zomato.app.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final WalletService walletService;

    public PaymentService(PaymentRepository paymentRepository, OrderService orderService, WalletService walletService) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
        this.walletService = walletService;
    }

    public Payment pay(Long orderId) {
        orderService.markPaid(orderId);
        return paymentRepository.save(new Payment(orderId, "MOCK_RAZORPAY", "SUCCESS", "pay_" + UUID.randomUUID()));
    }

    public Payment payWithWallet(String email, Long orderId) {
        double amount = orderService.getOrder(orderId).getTotalAmount();
        walletService.debit(email, amount);
        orderService.markPaid(orderId);
        return paymentRepository.save(new Payment(orderId, "WALLET", "SUCCESS", "wallet_" + UUID.randomUUID()));
    }
}
