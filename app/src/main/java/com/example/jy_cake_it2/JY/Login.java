package com.example.jy_cake_it2.JY;

public class Login {
    private String phone;
    private int id;
    private String name;

    private String address;

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
        return name;
    }
    public void setStoreName(String storeName) {
        this.name = storeName;
    }
    public String getStoreAddress(){
        return address;
    }
    public void setStoreAddress(String storeAddress) {
        this.address = storeAddress;
    }
    public Login(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }
}

