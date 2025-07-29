package com.example.dropshipping.model;

import java.util.List;

public class StoreCart {
    private int store_id;
    private String store_name;
    private List<CartItem> items;

    public StoreCart(int store_id, String store_name, List<CartItem> items) {
        this.store_id = store_id;
        this.store_name = store_name;
        this.items = items;
    }

    // Getters and setters
    public int getStore_id() { return store_id; }
    public String getStore_name() { return store_name; }
    public List<CartItem> getItems() { return items; }
}