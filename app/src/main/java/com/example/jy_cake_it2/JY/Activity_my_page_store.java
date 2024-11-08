package com.example.jy_cake_it2.JY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
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

public class Activity_my_page_store extends AppCompatActivity {
    private ImageView userPic;
    private TextView title_myPage,  id, phoneNum, text_id, text_phoneNum, store_name, text_storeName, store_address, text_storeAddress, sns, text_sns,bank, text_bank;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_shop);
        //가능하면 회원가입 단계에서 사진을 추가해서 넣어줄 수 있게 해주면 좋다.
        userPic = findViewById(R.id.user_pic);

        title_myPage = findViewById(R.id.title_myPage);

        id = findViewById(R.id.myPage_id);
        phoneNum = findViewById(R.id.myPage_phoneNum);
        store_name = findViewById(R.id.store_name);
        store_address = findViewById(R.id.store_address);
        sns = findViewById(R.id.SNS);
        bank = findViewById(R.id.bank);

        text_id = findViewById(R.id.text_id);
        text_phoneNum = findViewById(R.id.text_phoneNum);
        text_storeName = findViewById(R.id.text_storeName);
        text_storeAddress = findViewById(R.id.text_storeAddress);
        text_sns = findViewById(R.id.text_SNS);
        text_bank = findViewById(R.id.text_bank);

        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("AccessToken", null);
        if (accessToken != null) {
            Retrofit retrofit = RetrofitClient.getClient(accessToken);
            LoginApiService apiService = retrofit.create(LoginApiService.class);
            Call<StoreInfoResponse> call = apiService.getStoreInfo("Bearer " + accessToken);

            call.enqueue(new Callback<StoreInfoResponse>() {
                @Override
                public void onResponse(Call<StoreInfoResponse> call, Response<StoreInfoResponse> response) {
                    Toast.makeText(Activity_my_page_store.this, "code: " + response.code(), Toast.LENGTH_LONG).show();
                    if (response.isSuccessful() && response.body() != null) {
                        StoreInfoResponse userInfo = response.body();
                        text_id.setText(userInfo.getUsername());
                        text_phoneNum.setText(userInfo.getPhone());
                        text_storeName.setText(userInfo.getShopname());
                        text_storeAddress.setText(userInfo.getAddress());
                        text_sns.setText(userInfo.getSns());
                        text_bank.setText(userInfo.getBank());

                    } else {
                        Toast.makeText(Activity_my_page_store.this, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<StoreInfoResponse> call, Throwable t) {
                    Toast.makeText(Activity_my_page_store.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_mypage);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_shopPage) {
                    startActivity(new Intent(Activity_my_page_store.this, Shop_page.class));
                    return true;
                } else if (id == R.id.nav_bid_list) {
                    startActivity(new Intent(Activity_my_page_store.this, bids_shop.class));
                    return true;
                } else if (id == R.id.nav_order) {
                    startActivity(new Intent(Activity_my_page_store.this, shop_order.class));
                    return true;
                } else if (id == R.id.nav_mypage) {
                    startActivity(new Intent(Activity_my_page_store.this, Activity_my_page_store.class));
                    return true;
                }
                return false;
            }
        });

    }
}