package com.example.jy_cake_it2.JY;

public class ShopAccount {
    private String username;
    private String password1;
    private String password2;
    private String shopname;
    private String address;
    private String phone;
    private String email;
    public ShopAccount(String username, String password1, String password2, String shopname, String address, String phone, String email) {
        this.username = username;
        this.password1 = password1;
        this.password2 = password2;
        this.shopname = shopname;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword1() { return password1; }
    public String getPassword2() { return password2; }
    public String getShopname() { return shopname; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password1) {
        this.password1 = password1;
    }
    public void setPassword2(String password2) {
        this.password2 = password2;
    }
    public void setShopname(String shopname) {
        this.shopname = shopname;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setEmail(String email) {
        this.email = email;
    }

}
