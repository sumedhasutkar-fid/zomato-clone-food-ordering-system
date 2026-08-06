package com.zomato.app.repository;

import com.zomato.app.entity.FavoriteFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteFoodRepository extends JpaRepository<FavoriteFood, Long> {

    List<FavoriteFood> findByUserEmail(String userEmail);

    Optional<FavoriteFood> findByUserEmailAndFoodId(String userEmail, Long foodId);
}
