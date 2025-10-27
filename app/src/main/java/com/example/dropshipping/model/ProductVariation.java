package com.example.dropshipping.model;

public class ProductVariation {
    private int variationId;
    private int productId;
    private String size;
    private String color;
    private double weight;
    private double length;
    private double width;
    private double height;
    private double price;
    private String currency;
    private String skuSuffix;
    private int stockQuantity;
    private boolean isActive;
    private double sellingPrice;
    private double convertedPrice;
    private String convertedCurrency;
    private String imageUrl;

    public ProductVariation(int variationId, int productId, String size, String color,
                            double weight, double length, double width, double height,
                            double price, String currency, String skuSuffix,
                            int stockQuantity, boolean isActive, double sellingPrice,
                            double convertedPrice, String convertedCurrency, String imageUrl) {
        this.variationId = variationId;
        this.productId = productId;
        this.size = size;
        this.color = color;
        this.weight = weight;
        this.length = length;
        this.width = width;
        this.height = height;
        this.price = price;
        this.currency = currency;
        this.skuSuffix = skuSuffix;
        this.stockQuantity = stockQuantity;
        this.isActive = isActive;
        this.sellingPrice = sellingPrice;
        this.convertedPrice = convertedPrice;
        this.convertedCurrency = convertedCurrency;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public int getVariationId() { return variationId; }
    public int getProductId() { return productId; }
    public String getSize() { return size; }
    public String getColor() { return color; }
    public double getWeight() { return weight; }
    public double getLength() { return length; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getPrice() { return price; }
    public String getCurrency() { return currency; }
    public String getSkuSuffix() { return skuSuffix; }
    public int getStockQuantity() { return stockQuantity; }
    public boolean isActive() { return isActive; }
    public double getSellingPrice() { return sellingPrice; }
    public double getConvertedPrice() { return convertedPrice; }
    public String getConvertedCurrency() { return convertedCurrency; }
    public String getImageUrl() { return imageUrl; }
}