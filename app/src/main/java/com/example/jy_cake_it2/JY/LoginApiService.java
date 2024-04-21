package com.example.jy_cake_it2.JY;

import com.example.jy_cake_it2.JY.Login;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginApiService {
//    @GET("shop")
//    Call<List<Login>> getShops();
    @POST("api/user/create") // POST 요청
    Call<ApiResponse> createAccount(@Body UserAccount user);

    @POST("api/user/login")
    Call<ApiResponse> login(@Body LoginRequest loginRequest);
}
