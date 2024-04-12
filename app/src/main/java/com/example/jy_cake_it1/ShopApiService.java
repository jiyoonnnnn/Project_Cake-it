package com.example.jy_cake_it1;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ShopApiService {
    @GET("shop")
    Call<List<Shop>> getShops();
}