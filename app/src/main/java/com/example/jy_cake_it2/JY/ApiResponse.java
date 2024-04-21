package com.example.jy_cake_it2.JY;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {
    @SerializedName("access_token") // JSON 필드 이름과 매핑
    private String accessToken;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("username")
    private String username;
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}
