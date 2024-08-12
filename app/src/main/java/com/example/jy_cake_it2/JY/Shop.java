package com.example.jy_cake_it2.JY;

public class Shop {
    private int id;
    private String username;
    private String shopname;
    private String email;
    private String phone;
    private String address;
    private String sns;
    private String intro;
    public int getId() { return id; }
    public String getAddress() { return address; }
    public String getUsername() { return username; }
    public String getShopname() {
        return this.shopname;
    }
    public Shop(String username, String shopname, String address) {
        this.username = username;
        this.shopname = shopname;
        this.address = address;
    }
//    @Override
//    public String toString() {
//        return "Shop{username=" + username + ", shopname=" + shopname +", address=" + address + "}";
//    }

}
