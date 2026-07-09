package com.zomato.app.controller;

import com.zomato.app.entity.Review;
import com.zomato.app.service.ReviewService;
import com.zomato.app.util.AuthUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService service;
    private final AuthUtil authUtil;

    public ReviewController(ReviewService service, AuthUtil authUtil) {
        this.service = service;
        this.authUtil = authUtil;
    }

    @GetMapping
    public List<Review> getReviews(@RequestParam(defaultValue = "1") Long restaurantId) {
        return service.getReviews(restaurantId);
    }

    @PostMapping
    public Review save(@RequestBody Review review) {
        return service.save(authUtil.currentUserEmail(), review);
    }
}
