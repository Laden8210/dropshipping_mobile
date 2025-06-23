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
    @SerializedName("product_weight")
    private int productWeight;
    @SerializedName("status")
    private String status;
    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("profit_margin")
    private String profitMargin;

    @SerializedName("price")
    private String price;

    @SerializedName("currency")
    private String currency;

    @SerializedName("change_date")
    private String changeDate;

    @SerializedName("primary_image")
    private String primaryImage;

    @SerializedName("warehouse_name")
    private String warehouseName;

    @SerializedName("warehouse_address")
    private String warehouseAddress;

    @SerializedName("current_stock")
    private int currentStock;

    @SerializedName("converted_price")
    private double convertedPrice;

    @SerializedName("converted_currency")
    private String convertedCurrency;

    @SerializedName("selling_price")
    private double sellingPrice;

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

    public int getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(int productWeight) {
        this.productWeight = productWeight;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
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

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public double getConvertedPrice() {
        return convertedPrice;
    }

    public void setConvertedPrice(double convertedPrice) {
        this.convertedPrice = convertedPrice;
    }

    public String getConvertedCurrency() {
        return convertedCurrency;
    }

    public void setConvertedCurrency(String convertedCurrency) {
        this.convertedCurrency = convertedCurrency;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
