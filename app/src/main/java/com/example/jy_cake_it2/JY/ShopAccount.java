package com.example.jy_cake_it2.JY;

public class ShopAccount {
    private String username;
    private String password1;
    private String password2;
    private String shopname;
    private String address;
    private String bank;
    private String phone;
    private String email;
    private String loc_x;
    private String loc_y;
    public ShopAccount(String username, String email,String password1, String password2, String shopname, String phone, String address,  String loc_x, String loc_y, String bank) {
        this.username = username;
        this.password1 = password1;
        this.password2 = password2;
        this.shopname = shopname;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.loc_x = loc_x;
        this.loc_y = loc_y;
        this.bank= bank;
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
    public String getBank() { return bank; }
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
