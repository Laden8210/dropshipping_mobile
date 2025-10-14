package com.example.dropshipping.model;

public class CartProduct {

        private String cartId;
        private String id;
        private String variantId;
        private String name;
        private String attributes;
        private double price;
        private String imageRes;
        private int quantity;
        private double weight;
        private double length;
        private boolean isSelected;

        public CartProduct(String cartid, String id, String variantId, String name, String attributes, double price, String imageRes, int quantity) {
            this.cartId = cartid;
            this.id = id;
            this.variantId = variantId;
            this.name = name;
            this.attributes = attributes;
            this.price = price;
            this.imageRes = imageRes;
            this.quantity = quantity;
            this.isSelected = false;
        }

        // Getters and setters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getAttributes() { return attributes; }
        public double getPrice() { return price; }
        public String getImageRes() { return imageRes; }
        public int getQuantity() { return quantity; }
        public boolean isSelected() { return isSelected; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public void setSelected(boolean selected) { isSelected = selected; }
        public String getVariantId() { return variantId; }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageRes(String imageRes) {
        this.imageRes = imageRes;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }
}
