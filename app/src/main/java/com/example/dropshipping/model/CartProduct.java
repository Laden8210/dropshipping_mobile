package com.example.dropshipping.model;

public class CartProduct {

        private String id;
        private String name;
        private String attributes;
        private double price;
        private String imageRes;
        private int quantity;
        private boolean isSelected;

        public CartProduct(String id, String name, String attributes, double price, String imageRes, int quantity) {
            this.id = id;
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


}
