package com.zomato.app.service;

import com.zomato.app.entity.Review;
import com.zomato.app.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository repository;

    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }

    public List<Review> getReviews(Long restaurantId) {
        return repository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId);
    }

    public Review save(String email, Review review) {
        review.setUserEmail(email);
        review.setCreatedAt(LocalDateTime.now());
        return repository.save(review);
    }
}
