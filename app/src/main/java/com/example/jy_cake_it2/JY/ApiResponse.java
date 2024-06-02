package com.example.jy_cake_it2.JY;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {
    @SerializedName("access_token") // JSON 필드 이름과 매핑
    private String accessToken;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("username")
    private String username;
    @SerializedName("shopname")
    private String shopname;
    @SerializedName("id")
    private int id;
    @SerializedName("shopdetail")
    private String shopdetail;
    @SerializedName("shop_list")
    private List<Shop> shop_list;
    public String getAccessToken() {
        return accessToken;
    }
    public List<Shop> getShop_list() {
        return shop_list;
    }
    public int getId() { return id; }
//    public String user(int id, String username){
//        this.id = id;
//        this.username = username;
//    }
    public String getShopname() {
        return this.shopname;
    }
    public void setShopname(String shopname) { this.shopname = shopname; }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}
