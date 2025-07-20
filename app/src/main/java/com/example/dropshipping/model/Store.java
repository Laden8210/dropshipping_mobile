package com.example.dropshipping.model;

import java.util.List;


public class Store {
    private String id;
    private String name;
    private List<CartProduct> products;
    private double shippingFee;

    public Store(String id, String name, List<CartProduct> products, double shippingFee) {
        this.id = id;
        this.name = name;
        this.products = products;
        this.shippingFee = shippingFee;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public List<CartProduct> getProducts() { return products; }
    public double getShippingFee() { return shippingFee; }
}