package com.example.jy_cake_it1;

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
        return this.phone;
    }
    public int getId(){
        return this.id;
    }
    public String getStoreName(){
        return this.storeName;
    }
    public String getStoreAddress(){
        return this.storeAddress;
    }
}
