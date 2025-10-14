package com.example.dropshipping.model;

public class CartItem {
    private int cart_id;
    private int variation_id;
    private int quantity;
    private String created_at;
    private String updated_at;
    private String product_name;
    private String product_sku;
    private String description;
    private String product_image;
    private int product_id;
    private int storeId;
    private double price;

    private double selling_price;

    public CartItem(int cart_id, int quantity, String created_at, String updated_at,
                    String product_name, String product_sku, String description, String product_image) {
        this.cart_id = cart_id;
        this.quantity = quantity;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.product_name = product_name;
        this.product_sku = product_sku;
        this.description = description;
        this.product_image = product_image;
    }


    public CartItem(int product_id, int variation_id, double price, int quantity, int storeId) {
        this.product_id = product_id;
        this.variation_id = variation_id;
        this.price = price;
        this.quantity = quantity;
        this.storeId = storeId;
    }

    // Getters and setters
    public int getCart_id() { return cart_id; }
    public int getQuantity() { return quantity; }
    public String getCreated_at() { return created_at; }
    public String getUpdated_at() { return updated_at; }
    public String getProduct_name() { return product_name; }
    public String getProduct_sku() { return product_sku; }
    public String getDescription() { return description; }
    public String getProduct_image() { return product_image; }
    public int getProduct_id() { return product_id; }
    public int getStoreId() { return storeId; }
    public double getPrice() { return price; }
    public void setCart_id(int cart_id) { this.cart_id = cart_id; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }
    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }
    public void setProduct_name(String product_name) { this.product_name = product_name; }
    public void setProduct_sku(String product_sku) { this.product_sku = product_sku; }
    public void setDescription(String description) { this.description = description; }
    public void setProduct_image(String product_image) { this.product_image = product_image; }
    public double getSelling_price() { return selling_price; }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setSelling_price(double selling_price) {
        this.selling_price = selling_price;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getVariation_id() {
        return variation_id;
    }

    public void setVariation_id(int variation_id) {
        this.variation_id = variation_id;
    }
}