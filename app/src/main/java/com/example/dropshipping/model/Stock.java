package com.example.dropshipping.model;

import com.google.gson.annotations.SerializedName;

public class Stock {
    @SerializedName("areaEn")
    private String areaEn;

    @SerializedName("areaId")
    private int areaId;

    @SerializedName("countryCode")
    private String countryCode;

    @SerializedName("totalInventoryNum")
    private int totalInventoryNum;

    @SerializedName("cjInventoryNum")
    private int cjInventoryNum;

    @SerializedName("factoryInventoryNum")
    private int factoryInventoryNum;

    @SerializedName("countryNameEn")
    private String countryNameEn;


    public String getAreaEn() {
        return areaEn;
    }

    public void setAreaEn(String areaEn) {
        this.areaEn = areaEn;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getTotalInventoryNum() {
        return totalInventoryNum;
    }

    public void setTotalInventoryNum(int totalInventoryNum) {
        this.totalInventoryNum = totalInventoryNum;
    }

    public int getCjInventoryNum() {
        return cjInventoryNum;
    }

    public void setCjInventoryNum(int cjInventoryNum) {
        this.cjInventoryNum = cjInventoryNum;
    }

    public int getFactoryInventoryNum() {
        return factoryInventoryNum;
    }

    public void setFactoryInventoryNum(int factoryInventoryNum) {
        this.factoryInventoryNum = factoryInventoryNum;
    }

    public String getCountryNameEn() {
        return countryNameEn;
    }

    public void setCountryNameEn(String countryNameEn) {
        this.countryNameEn = countryNameEn;
    }
}
