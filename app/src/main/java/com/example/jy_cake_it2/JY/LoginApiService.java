package com.example.jy_cake_it2.JY;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginApiService {
    @GET("api/shop/list")
    Call<ApiResponse> getShops();
    @POST("api/user/create") // POST 요청
    Call<ApiResponse> createAccount(@Body UserAccount user);
    @POST("api/shop/create") // POST 요청
    Call<ApiResponse> createShopAccount(@Body ShopAccount shop);
    @FormUrlEncoded
    @POST("api/user/login")
    Call<ApiResponse> login(@Field("username") String username, @Field("password") String password); // 폼 필드로 전송
    @FormUrlEncoded
    @POST("api/shop/login")
    Call<ApiResponse> shoplogin(@Field("username") String username, @Field("password") String password); // 폼 필드로 전송
}
