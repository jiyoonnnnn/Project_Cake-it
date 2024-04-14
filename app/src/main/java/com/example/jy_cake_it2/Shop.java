package com.example.jy_cake_it2;

import com.google.gson.annotations.SerializedName;

public class Shop {
    @SerializedName("phone")
    private String phone;

    @SerializedName("num")
    private int id;

    @SerializedName("name")
    private String storeName;

    @SerializedName("address")
    private String storeAddress;

    public String getPhone(){
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public int getId(){
        return id;
    }

    public String getStoreName(){
        return storeName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    public String getStoreAddress(){
        return storeAddress;
    }
    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }
    public Shop(String phone, String storeName, String storeAddress) {
        this.phone = phone;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
    }
}
