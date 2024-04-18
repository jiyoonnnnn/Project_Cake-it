package com.example.jy_cake_it2.JY;

import com.example.jy_cake_it2.JY.Login;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginApiService {
    //    @GET("shop")
//    Call<List<Shop>> getShops();
    @POST("shop/") // POST 요청
    Call<ResponseBody> createPost(@Body Login data);
}