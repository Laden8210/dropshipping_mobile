package com.example.dropshipping.model;

import com.google.gson.annotations.SerializedName;

public class OrderItem {
    private String name;
    private double price;
    private int quantity;
    private String imageUrl;
    private String description;
    private String sku;
    private String categoryName;
    private long productId;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }
}
