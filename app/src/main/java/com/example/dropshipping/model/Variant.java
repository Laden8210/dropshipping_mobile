package com.example.dropshipping.model;

import com.google.gson.annotations.SerializedName;

public class Variant {
    @SerializedName("vid")
    private String vid;

    @SerializedName("pid")
    private String pid;

    @SerializedName("variantNameEn")
    private String variantNameEn;

    @SerializedName("variantImage")
    private String variantImage;

    @SerializedName("variantSku")
    private String variantSku;

    @SerializedName("variantUnit")
    private String variantUnit;

    @SerializedName("variantKey")
    private String variantKey;

    @SerializedName("variantLength")
    private int variantLength;

    @SerializedName("variantWidth")
    private int variantWidth;

    @SerializedName("variantHeight")
    private int variantHeight;

    @SerializedName("variantVolume")
    private int variantVolume;

    @SerializedName("variantWeight")
    private int variantWeight;

    @SerializedName("variantSellPrice")
    private double variantSellPrice;

    @SerializedName("createTime")
    private long createTime;

    @SerializedName("variantStandard")
    private String variantStandard;

    @SerializedName("variantSugSellPrice")
    private double variantSugSellPrice;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getVariantNameEn() {
        return variantNameEn;
    }

    public void setVariantNameEn(String variantNameEn) {
        this.variantNameEn = variantNameEn;
    }

    public String getVariantImage() {
        return variantImage;
    }

    public void setVariantImage(String variantImage) {
        this.variantImage = variantImage;
    }

    public String getVariantSku() {
        return variantSku;
    }

    public void setVariantSku(String variantSku) {
        this.variantSku = variantSku;
    }

    public String getVariantUnit() {
        return variantUnit;
    }

    public void setVariantUnit(String variantUnit) {
        this.variantUnit = variantUnit;
    }

    public String getVariantKey() {
        return variantKey;
    }

    public void setVariantKey(String variantKey) {
        this.variantKey = variantKey;
    }

    public int getVariantLength() {
        return variantLength;
    }

    public void setVariantLength(int variantLength) {
        this.variantLength = variantLength;
    }

    public int getVariantWidth() {
        return variantWidth;
    }

    public void setVariantWidth(int variantWidth) {
        this.variantWidth = variantWidth;
    }

    public int getVariantHeight() {
        return variantHeight;
    }

    public void setVariantHeight(int variantHeight) {
        this.variantHeight = variantHeight;
    }

    public int getVariantVolume() {
        return variantVolume;
    }

    public void setVariantVolume(int variantVolume) {
        this.variantVolume = variantVolume;
    }

    public int getVariantWeight() {
        return variantWeight;
    }

    public void setVariantWeight(int variantWeight) {
        this.variantWeight = variantWeight;
    }

    public double getVariantSellPrice() {
        return variantSellPrice;
    }

    public void setVariantSellPrice(double variantSellPrice) {
        this.variantSellPrice = variantSellPrice;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getVariantStandard() {
        return variantStandard;
    }

    public void setVariantStandard(String variantStandard) {
        this.variantStandard = variantStandard;
    }

    public double getVariantSugSellPrice() {
        return variantSugSellPrice;
    }

    public void setVariantSugSellPrice(double variantSugSellPrice) {
        this.variantSugSellPrice = variantSugSellPrice;
    }
}

