package com.example.dropshipping.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class CheckoutProduct implements Parcelable {

     private int pid;
    private String name;
    private double price;
    private int quantity;

    private int storeId;

    public CheckoutProduct(int pid, String name, double price, int quantity, int storeId) {
        this.pid = pid;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.storeId = storeId;
    }

    protected CheckoutProduct(Parcel in) {
        pid = in.readInt();
        name = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
        storeId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pid);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeInt(quantity);
        dest.writeInt(storeId);
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

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
