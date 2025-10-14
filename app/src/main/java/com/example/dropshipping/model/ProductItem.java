package com.example.dropshipping.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProductItem {

    @SerializedName("product_id")
    private int pid;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("product_sku")
    private String productSku;

    @SerializedName("status")
    private String status;

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("profit_margin")
    private String profitMargin;

    @SerializedName("min_price")
    private String minPrice;

    @SerializedName("max_price")
    private String maxPrice;

    @SerializedName("currency")
    private String currency;

    @SerializedName("total_stock")
    private int totalStock;

    @SerializedName("primary_image")
    private String primaryImage;

    @SerializedName("warehouse_name")
    private String warehouseName;

    @SerializedName("warehouse_address")
    private String warehouseAddress;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("min_converted_price")
    private double minConvertedPrice;

    @SerializedName("min_converted_currency")
    private String minConvertedCurrency;

    @SerializedName("min_selling_price")
    private double minSellingPrice;

    @SerializedName("max_converted_price")
    private double maxConvertedPrice;

    @SerializedName("max_converted_currency")
    private String maxConvertedCurrency;

    @SerializedName("max_selling_price")
    private double maxSellingPrice;

    @SerializedName("store_id")
    private int storeId;

    // Getters and Setters
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(String profitMargin) {
        this.profitMargin = profitMargin;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(int totalStock) {
        this.totalStock = totalStock;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public double getMinConvertedPrice() {
        return minConvertedPrice;
    }

    public void setMinConvertedPrice(double minConvertedPrice) {
        this.minConvertedPrice = minConvertedPrice;
    }

    public String getMinConvertedCurrency() {
        return minConvertedCurrency;
    }

    public void setMinConvertedCurrency(String minConvertedCurrency) {
        this.minConvertedCurrency = minConvertedCurrency;
    }

    public double getMinSellingPrice() {
        return minSellingPrice;
    }

    public void setMinSellingPrice(double minSellingPrice) {
        this.minSellingPrice = minSellingPrice;
    }

    public double getMaxConvertedPrice() {
        return maxConvertedPrice;
    }

    public void setMaxConvertedPrice(double maxConvertedPrice) {
        this.maxConvertedPrice = maxConvertedPrice;
    }

    public String getMaxConvertedCurrency() {
        return maxConvertedCurrency;
    }

    public void setMaxConvertedCurrency(String maxConvertedCurrency) {
        this.maxConvertedCurrency = maxConvertedCurrency;
    }

    public double getMaxSellingPrice() {
        return maxSellingPrice;
    }

    public void setMaxSellingPrice(double maxSellingPrice) {
        this.maxSellingPrice = maxSellingPrice;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    // Helper method to get display price
    public String getDisplayPrice() {
        if (minSellingPrice == maxSellingPrice) {
            return String.format("₱%.2f", minSellingPrice);
        } else {
            return String.format("₱%.2f - ₱%.2f", minSellingPrice, maxSellingPrice);
        }
    }

    // Helper method to check if product has stock
    public boolean hasStock() {
        return totalStock > 0;
    }

    // Helper method to get stock status
    public String getStockStatus() {
        if (totalStock == 0) {
            return "Out of Stock";
        } else if (totalStock < 10) {
            return "Low Stock";
        } else {
            return "In Stock";
        }
    }
}