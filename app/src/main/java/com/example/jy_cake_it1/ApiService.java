package com.example.jy_cake_it1;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("/shop/")
    Call<MainActivity.Shop> getName();

//    @POST("/shop/")
//    Call<MainActivity.Shop> createShop(@Body RequestBody requestBody);
}