package com.zomato.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "favorite_foods")
public class FavoriteFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;
    private Long foodId;
    private String foodName;

    public FavoriteFood() {
    }

    public FavoriteFood(String userEmail, Long foodId, String foodName) {
        this.userEmail = userEmail;
        this.foodId = foodId;
        this.foodName = foodName;
    }

    public Long getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public Long getFoodId() { return foodId; }
    public String getFoodName() { return foodName; }
}
