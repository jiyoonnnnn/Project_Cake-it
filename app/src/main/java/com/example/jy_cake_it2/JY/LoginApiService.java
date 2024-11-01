package com.example.jy_cake_it2.JY;

import com.google.android.gms.common.api.Api;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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
    @Multipart
    @POST("api/order/create")
    Call<Detail> createDetail(@Header("Authorization") String authHeader,
                              @Part("subject") RequestBody subject,
                              @Part("content") RequestBody content,
                              @Part("cake_type") RequestBody cakeType,
                              @Part("cake_shape") RequestBody cakeShape,
                              @Part("cake_color") RequestBody cakeColor,
                              @Part("cake_flavor") RequestBody cakeFlavor,
                              @Part("pickup_date") RequestBody pickupDate,
                              @Part("lettering") RequestBody lettering,
                              @Part("shop_id") RequestBody shopId,
                              @Part MultipartBody.Part file);
    @GET("api/order/detail/{question_id}")
    Call<Detail> getDetail(@Path("question_id") int question_id);
    @GET("api/order/view/{file_name}")
    Call<ResponseBody> getImage(@Path("file_name") String file_name);
    @GET("api/user/mypage")
    Call<UserInfoResponse> getUserInfo(@Header("Authorization") String authHeader);
    @GET("api/shop/mypage")
    Call<StoreInfoResponse> getStoreInfo(@Header("Authorization") String authHeader);
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
    @PUT("api/order/select")
    Call<SelectShop> selectShop(@Header("Authorization") String authHeader, @Body SelectShop request);
    @PUT("api/order/status")
    Call<ChangeStatus> changeStatus(@Header("Authorization") String authHeader, @Body ChangeStatus request);
    @PUT("api/order/accept")
    Call<Detail> orderAccept(@Header("Authorization") String authHeader, @Body OrderRequest orderRequest);
    @PUT("api/order/deny")
    Call<Detail> orderDeny(@Header("Authorization") String authHeader, @Body DenyRequest denyRequest);
}
