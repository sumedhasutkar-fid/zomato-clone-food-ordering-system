package com.zomato.app.service;

import com.zomato.app.entity.Coupon;
import com.zomato.app.exception.InvalidRequestException;
import com.zomato.app.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CouponService {

    private final CouponRepository repository;

    public CouponService(CouponRepository repository) {
        this.repository = repository;
    }

    public Coupon save(Coupon coupon) {
        coupon.setActive(true);
        return repository.save(coupon);
    }

    public List<Coupon> all() {
        return repository.findAll();
    }

    public Map<String, Object> apply(String code, double amount) {
        Coupon coupon = repository.findByCodeIgnoreCaseAndActiveTrue(code)
                .orElseThrow(() -> new InvalidRequestException("Invalid coupon"));
        double discount = amount * coupon.getDiscountPercent() / 100;
        return Map.of(
                "code", coupon.getCode(),
                "discountPercent", coupon.getDiscountPercent(),
                "discountAmount", discount,
                "finalAmount", Math.max(amount - discount, 0)
        );
    }
}
