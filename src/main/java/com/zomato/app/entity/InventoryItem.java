package com.zomato.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory_items")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long foodId;
    private String itemName;
    private int stockQuantity;

    public InventoryItem() {
    }

    public InventoryItem(Long foodId, String itemName, int stockQuantity) {
        this.foodId = foodId;
        this.itemName = itemName;
        this.stockQuantity = stockQuantity;
    }

    public Long getId() { return id; }
    public Long getFoodId() { return foodId; }
    public String getItemName() { return itemName; }
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
}
