package com.zomato.app.repository;

import com.zomato.app.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUserEmail(String userEmail);

    Optional<CartItem> findByUserEmailAndFoodId(String userEmail, Long foodId);

    void deleteByUserEmail(String userEmail);

    void deleteByUserEmailAndFoodId(String userEmail, Long foodId);
}
