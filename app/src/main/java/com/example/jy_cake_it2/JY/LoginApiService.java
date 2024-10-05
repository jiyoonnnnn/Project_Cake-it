package com.example.jy_cake_it2.JY;

import com.google.android.gms.common.api.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface LoginApiService {
    @GET("api/shop/list")
    Call<ApiResponse> getShops();
    @GET("api/user/myorder")
    Call<ApiResponse> getUserOrders(@Header("Authorization") String authHeader);
    @GET("api/shop/myorder")
    Call<ApiResponse> getShopOrders(@Header("Authorization") String authHeader);
    @PUT("api/order/select")
    Call<UpdateRequest> updateShop(@Header("Authorization") String authHeader, @Body UpdateRequest body);
    @POST("api/order/create")
    Call<Detail> createDetail(@Header("Authorization") String authHeader, @Body Detail body);
    @GET("api/order/detail/{question_id}")
    Call<Detail> getDetail(@Path("question_id") int question_id);
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
    @POST("api/bid/create/{question_id}")
    Call<ApiResponse> createBid(@Header("Authorization") String authHeader, @Path("question_id") int question_id, @Body Bids bids);
    @GET("api/order/list")
    Call<ApiResponse> getOrderList();
}
