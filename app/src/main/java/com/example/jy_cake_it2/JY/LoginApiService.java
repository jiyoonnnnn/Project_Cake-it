package com.example.jy_cake_it2.JY;

import com.example.jy_cake_it2.JY.Login;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginApiService {
//    @GET("shop")
//    Call<List<Login>> getShops();
    @POST("api/user/create") // POST 요청
    Call<ApiResponse> createAccount(@Body UserAccount user);
    @FormUrlEncoded
    @POST("api/user/login")
    Call<ApiResponse> login(@Field("username") String username, @Field("password") String password); // 폼 필드로 전송
}
