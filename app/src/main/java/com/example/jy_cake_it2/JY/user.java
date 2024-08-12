package com.example.jy_cake_it2.JY;

public class User {
    private int id;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String sns;
    private String intro;
    public int getId() { return id; }
    public String getAddress() { return address; }
    public String getUsername() { return username; }
    public String getEmail() {
        return email;
    }
    public User(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
