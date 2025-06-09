package com.example.dropshipping.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProductItem {

    @SerializedName("product_id")
    private int productId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("pid")
    private String pid;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("supplier_id")
    private String supplierId;

    @SerializedName("product_sku")
    private String productSku;

    @SerializedName("category")
    private String category;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("status")
    private String status;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("productNameSet")
    private List<String> productNameSet;

    @SerializedName("productNameEn")
    private String productNameEn;

    @SerializedName("productSku")
    private String productSkuDuplicate;

    @SerializedName("productImageSet")
    private List<String> productImageSet;

    @SerializedName("productWeight")
    private String productWeight;

    @SerializedName("productUnit")
    private String productUnit;

    @SerializedName("productType")
    private String productType;

    @SerializedName("categoryId")
    private String categoryId;

    @SerializedName("categoryName")
    private String categoryName;

    @SerializedName("entryCode")
    private String entryCode;

    @SerializedName("entryName")
    private String entryName;

    @SerializedName("entryNameEn")
    private String entryNameEn;

    @SerializedName("materialNameSet")
    private List<String> materialNameSet;

    @SerializedName("materialNameEnSet")
    private List<String> materialNameEnSet;

    @SerializedName("materialKeySet")
    private List<String> materialKeySet;

    @SerializedName("packingWeight")
    private String packingWeight;

    @SerializedName("packingNameSet")
    private List<String> packingNameSet;

    @SerializedName("packingNameEnSet")
    private List<String> packingNameEnSet;

    @SerializedName("packingKeySet")
    private List<String> packingKeySet;

    @SerializedName("productKeySet")
    private List<String> productKeySet;

    @SerializedName("productKeyEn")
    private String productKeyEn;

    @SerializedName("productProSet")
    private List<String> productProSet;

    @SerializedName("productProEnSet")
    private List<String> productProEnSet;

    @SerializedName("sellPrice")
    private String sellPrice;

    @SerializedName("sourceFrom")
    private int sourceFrom;

    @SerializedName("description")
    private String description;

    @SerializedName("variants")
    private List<Variant> variants;

    @SerializedName("addMarkStatus")
    private int addMarkStatus;

    @SerializedName("createrTime")
    private String createrTime;

    @SerializedName("productVideo")
    private String productVideo;

    @SerializedName("suggestSellPrice")
    private String suggestSellPrice;

    @SerializedName("listedNum")
    private int listedNum;

    @SerializedName("supplierName")
    private String supplierName;

    @SerializedName("stock")
    private List<Stock> stock;

    @SerializedName("totalInventory")
    private int totalInventory;

    @SerializedName("exchangeRate")
    private double exchangeRate;

    @SerializedName("status_db")
    private String statusDb;


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<String> getProductNameSet() {
        return productNameSet;
    }

    public void setProductNameSet(List<String> productNameSet) {
        this.productNameSet = productNameSet;
    }

    public String getProductNameEn() {
        return productNameEn;
    }

    public void setProductNameEn(String productNameEn) {
        this.productNameEn = productNameEn;
    }

    public String getProductSkuDuplicate() {
        return productSkuDuplicate;
    }

    public void setProductSkuDuplicate(String productSkuDuplicate) {
        this.productSkuDuplicate = productSkuDuplicate;
    }

    public List<String> getProductImageSet() {
        return productImageSet;
    }

    public void setProductImageSet(List<String> productImageSet) {
        this.productImageSet = productImageSet;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getEntryCode() {
        return entryCode;
    }

    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getEntryNameEn() {
        return entryNameEn;
    }

    public void setEntryNameEn(String entryNameEn) {
        this.entryNameEn = entryNameEn;
    }

    public List<String> getMaterialNameSet() {
        return materialNameSet;
    }

    public void setMaterialNameSet(List<String> materialNameSet) {
        this.materialNameSet = materialNameSet;
    }

    public List<String> getMaterialNameEnSet() {
        return materialNameEnSet;
    }

    public void setMaterialNameEnSet(List<String> materialNameEnSet) {
        this.materialNameEnSet = materialNameEnSet;
    }

    public List<String> getMaterialKeySet() {
        return materialKeySet;
    }

    public void setMaterialKeySet(List<String> materialKeySet) {
        this.materialKeySet = materialKeySet;
    }

    public String getPackingWeight() {
        return packingWeight;
    }

    public void setPackingWeight(String packingWeight) {
        this.packingWeight = packingWeight;
    }

    public List<String> getPackingNameSet() {
        return packingNameSet;
    }

    public void setPackingNameSet(List<String> packingNameSet) {
        this.packingNameSet = packingNameSet;
    }

    public List<String> getPackingNameEnSet() {
        return packingNameEnSet;
    }

    public void setPackingNameEnSet(List<String> packingNameEnSet) {
        this.packingNameEnSet = packingNameEnSet;
    }

    public List<String> getPackingKeySet() {
        return packingKeySet;
    }

    public void setPackingKeySet(List<String> packingKeySet) {
        this.packingKeySet = packingKeySet;
    }

    public List<String> getProductKeySet() {
        return productKeySet;
    }

    public void setProductKeySet(List<String> productKeySet) {
        this.productKeySet = productKeySet;
    }

    public String getProductKeyEn() {
        return productKeyEn;
    }

    public void setProductKeyEn(String productKeyEn) {
        this.productKeyEn = productKeyEn;
    }

    public List<String> getProductProSet() {
        return productProSet;
    }

    public void setProductProSet(List<String> productProSet) {
        this.productProSet = productProSet;
    }

    public List<String> getProductProEnSet() {
        return productProEnSet;
    }

    public void setProductProEnSet(List<String> productProEnSet) {
        this.productProEnSet = productProEnSet;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getSourceFrom() {
        return sourceFrom;
    }

    public void setSourceFrom(int sourceFrom) {
        this.sourceFrom = sourceFrom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }

    public int getAddMarkStatus() {
        return addMarkStatus;
    }

    public void setAddMarkStatus(int addMarkStatus) {
        this.addMarkStatus = addMarkStatus;
    }

    public String getCreaterTime() {
        return createrTime;
    }

    public void setCreaterTime(String createrTime) {
        this.createrTime = createrTime;
    }

    public String getProductVideo() {
        return productVideo;
    }

    public void setProductVideo(String productVideo) {
        this.productVideo = productVideo;
    }

    public String getSuggestSellPrice() {
        return suggestSellPrice;
    }

    public void setSuggestSellPrice(String suggestSellPrice) {
        this.suggestSellPrice = suggestSellPrice;
    }

    public int getListedNum() {
        return listedNum;
    }

    public void setListedNum(int listedNum) {
        this.listedNum = listedNum;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public List<Stock> getStock() {
        return stock;
    }

    public void setStock(List<Stock> stock) {
        this.stock = stock;
    }

    public int getTotalInventory() {
        return totalInventory;
    }

    public void setTotalInventory(int totalInventory) {
        this.totalInventory = totalInventory;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getStatusDb() {
        return statusDb;
    }

    public void setStatusDb(String statusDb) {
        this.statusDb = statusDb;
    }
}
