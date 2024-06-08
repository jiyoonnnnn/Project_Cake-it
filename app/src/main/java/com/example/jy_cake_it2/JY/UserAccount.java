package com.example.jy_cake_it2.JY;

public class UserAccount {

    private String username;
    private String email;
    private String password1;
    private String password2;
    private String phone;
    public UserAccount(String username, String password1, String password2, String phone, String email) {
        this.username = username;
        this.password1 = password1;
        this.password2 = password2;
        this.phone = phone;
        this.email = email;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword1() { return password1; }
    public String getPassword2() { return password2; }
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
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setEmail(String email) {
        this.email = email;
    }

//    public int getId(){
//        return id;
//    }
//
//    public String getStoreName(){
//        return name;
//    }
//    public void setStoreName(String storeName) {
//        this.name = storeName;
//    }
//    public String getStoreAddress(){
//        return address;
//    }
//    public void setStoreAddress(String storeAddress) {
//        this.address = storeAddress;
//    }
}
