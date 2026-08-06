package com.zomato.app.controller;

import com.zomato.app.entity.Coupon;
import com.zomato.app.service.CouponService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/coupons")
@CrossOrigin(origins = "*")
public class CouponController {

    private final CouponService service;

    public CouponController(CouponService service) {
        this.service = service;
    }

    @GetMapping
    public List<Coupon> all() {
        return service.all();
    }

    @PostMapping
    public Coupon save(@RequestBody Coupon coupon) {
        return service.save(coupon);
    }

    @PostMapping("/apply")
    public Map<String, Object> apply(@RequestBody Map<String, Object> request) {
        return service.apply(request.get("code").toString(), Double.parseDouble(request.get("amount").toString()));
    }
}
