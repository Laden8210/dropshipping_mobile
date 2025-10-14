package com.example.dropshipping.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class CheckoutProduct implements Parcelable {

    private int pid;
    private int variantId;
    private String name;
    private double price;
    private int quantity;
    private double weight;
    private double length;

    private int storeId;
    private String cartId;


    public CheckoutProduct(int pid, int variantId, String name, double price, int quantity, double weight, double length,int storeId) {
        this.pid = pid;
        this.variantId = variantId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.storeId = storeId;
        this.weight = weight;
        this.length = length;
    }

    public CheckoutProduct(String cartId, int pid, int variantId, String name, double price, int quantity, double weight, double length,int storeId) {
        this.cartId = cartId;
        this.pid = pid;
        this.variantId = variantId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.storeId = storeId;
        this.weight = weight;
        this.length = length;
    }

    protected CheckoutProduct(Parcel in) {
        pid = in.readInt();
        variantId = in.readInt();
        name = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
        storeId = in.readInt();
        weight = in.readDouble();
        length = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pid);
        dest.writeInt(variantId);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeInt(quantity);
        dest.writeInt(storeId);
        dest.writeDouble(weight);
        dest.writeDouble(length);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CheckoutProduct> CREATOR = new Creator<CheckoutProduct>() {
        @Override
        public CheckoutProduct createFromParcel(Parcel in) {
            return new CheckoutProduct(in);
        }

        @Override
        public CheckoutProduct[] newArray(int size) {
            return new CheckoutProduct[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
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
