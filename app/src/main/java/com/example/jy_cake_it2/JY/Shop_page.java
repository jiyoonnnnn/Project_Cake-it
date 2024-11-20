package com.example.jy_cake_it2.JY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jy_cake_it2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Shop_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_page);
        TextView shopnameTextView = findViewById(R.id.text_shopname);
        TextView emailTextView = findViewById(R.id.text_email);
        TextView phoneTextView = findViewById(R.id.text_phone);
        TextView addressTextView = findViewById(R.id.text_address);
        TextView snsTextView = findViewById(R.id.text_sns);
        TextView introTextView = findViewById(R.id.text_intro);
        TextView bankTextView = findViewById(R.id.text_bank);
        LinearLayout layoutShopname = findViewById(R.id.layout_shopname);
        LinearLayout layoutEmail = findViewById(R.id.layout_email);
        LinearLayout layoutPhone = findViewById(R.id.layout_phone);
        LinearLayout layoutAddress = findViewById(R.id.layout_address);
        LinearLayout layoutSns = findViewById(R.id.layout_sns);
        LinearLayout layoutIntro = findViewById(R.id.layout_intro);
        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("AccessToken", null);
        if (accessToken != null) {
            Retrofit retrofit = RetrofitClient.getClient(accessToken);
            LoginApiService apiService = retrofit.create(LoginApiService.class);
            Call<StoreInfoResponse> call = apiService.getStoreInfo("Bearer " + accessToken);
            call.enqueue(new Callback<StoreInfoResponse>() {
                @Override
                public void onResponse(Call<StoreInfoResponse> call, Response<StoreInfoResponse> response) {
                    Toast.makeText(Shop_page.this, "code: " + response.code(), Toast.LENGTH_LONG).show();
                    if (response.isSuccessful() && response.body() != null) {
                        StoreInfoResponse userInfo = response.body();
                        phoneTextView.setText(userInfo.getPhone());
                        shopnameTextView.setText(userInfo.getShopname());
                        addressTextView.setText(userInfo.getAddress());
                        snsTextView.setText(userInfo.getSns());
                        bankTextView.setText(userInfo.getBank());
                        introTextView.setText(userInfo.getIntro());
                        emailTextView.setText(userInfo.getEmail());
                        if (userInfo.getAddress() != null && !userInfo.getAddress().isEmpty()) {
                            addressTextView.setText(userInfo.getAddress());
                        } else {
                            layoutAddress.setVisibility(View.GONE);
                        }

                        // SNS
                        if (userInfo.getSns() != null && !userInfo.getSns().isEmpty()) {
                            snsTextView.setText(userInfo.getSns());
                        } else {
                            layoutSns.setVisibility(View.GONE);
                        }

                        // 소개
                        if (userInfo.getIntro() != null && !userInfo.getIntro().isEmpty()) {
                            introTextView.setText(userInfo.getIntro());
                        } else {
                            layoutIntro.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(Shop_page.this, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<StoreInfoResponse> call, Throwable t) {
                    Toast.makeText(Shop_page.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        ViewCompat.setOnApplyWindowInsetsListener(bottomNavigationView, (view, insets) -> {
            // 하단 네비게이션 바 높이만큼 패딩 적용
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            bottomNavigationView.setPadding(0, 0, 0, bottomInset);
            return insets;
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_shopPage);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_shopPage) {
                    startActivity(new Intent(Shop_page.this, Shop_page.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_bid_list) {
                    startActivity(new Intent(Shop_page.this, bids_shop.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_order) {
                    startActivity(new Intent(Shop_page.this, shop_order.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_mypage) {
                    startActivity(new Intent(Shop_page.this, Activity_my_page_store.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
}