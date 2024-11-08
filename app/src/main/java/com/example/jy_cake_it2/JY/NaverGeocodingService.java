package com.example.jy_cake_it2.JY;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface NaverGeocodingService {
    @GET("map-geocode/v2/geocode")
    Call<GeocodingResponse> getGeocoding(
            @Header("X-NCP-APIGW-API-KEY-ID") String clientId,
            @Header("X-NCP-APIGW-API-KEY") String clientSecret,
            @Query("query") String address
    );
}
