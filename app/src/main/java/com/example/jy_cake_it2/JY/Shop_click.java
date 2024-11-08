package com.example.jy_cake_it2.JY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jy_cake_it2.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Shop_click extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ShopAdapter shopAdapter;
    private int question_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shop_click);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        shopAdapter = new ShopAdapter(new ArrayList<>(), this::updateShopDetail);
        recyclerView.setAdapter(shopAdapter);

        fetchShopList();
    }
    private void fetchShopList() {
        LoginApiService apiService = RetrofitClient.getApiService();
        Call<ApiResponse> call = apiService.getShops();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    List<Shop> shops = apiResponse.getShop_list();
                    shopAdapter.updateShops(shops);
                } else {
                    Toast.makeText(Shop_click.this, "Failed to fetch shops", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(Shop_click.this, "Error fetching shops: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateShopDetail(Shop shop) {
        int shop_id = shop.getId();

        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("AccessToken", null);

        if (accessToken != null) {
            Retrofit retrofit = RetrofitClient.getClient(accessToken);
            LoginApiService apiService = retrofit.create(LoginApiService.class);
            Call<ApiResponse> call = apiService.getUserOrders("Bearer " + accessToken);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse != null && apiResponse.getOrder_list() != null && !apiResponse.getOrder_list().isEmpty()) {
                            question_id = apiResponse.getOrder_list().get(0).getId();

                            UpdateRequest updateRequest = new UpdateRequest(question_id, shop_id);
                            updateShop(accessToken, updateRequest);
                        } else {
                            Toast.makeText(Shop_click.this, "No orders found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Shop_click.this, "Failed to fetch orders", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(Shop_click.this, "Error fetching orders: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(Shop_click.this, "No Access Token found", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateShop(String accessToken, UpdateRequest updateRequest) {
        Retrofit retrofit = RetrofitClient.getClient(accessToken);
        LoginApiService apiService = retrofit.create(LoginApiService.class);

        Call<UpdateRequest> call = apiService.updateShop("Bearer " + accessToken, updateRequest);
        call.enqueue(new Callback<UpdateRequest>() {
            @Override
            public void onResponse(Call<UpdateRequest> call, Response<UpdateRequest> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Shop_click.this, "Shop updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Shop_click.this, user_order.class);
                    intent.putExtra("DETAIL_ID", updateRequest.getQuestionId());
                    startActivity(intent);
                } else {
                    Toast.makeText(Shop_click.this, "업데이트 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateRequest> call, Throwable t) {
                Toast.makeText(Shop_click.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }
}