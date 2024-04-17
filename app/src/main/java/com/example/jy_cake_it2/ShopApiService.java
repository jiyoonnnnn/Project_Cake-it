package com.example.jy_cake_it2;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ShopApiService {
//    @GET("shop")
//    Call<List<Shop>> getShops();
    @POST("shop/") // POST 요청
    Call<ResponseBody> createPost(@Body Shop data);
}
