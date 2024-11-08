package com.example.jy_cake_it2.JY;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jy_cake_it2.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class user_shop_home extends AppCompatActivity {
    private TextView shopnameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private TextView addressTextView;
    private TextView snsTextView;
    private TextView introTextView;
    private TextView bankTextView;
    private LinearLayout layoutShopname;
    private LinearLayout layoutEmail;
    private LinearLayout layoutPhone;
    private LinearLayout layoutAddress;
    private LinearLayout layoutSns;
    private LinearLayout layoutIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_shop_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // View 초기화
        shopnameTextView = findViewById(R.id.text_shopname);
        emailTextView = findViewById(R.id.text_email);
        phoneTextView = findViewById(R.id.text_phone);
        addressTextView = findViewById(R.id.text_address);
        snsTextView = findViewById(R.id.text_sns);
        introTextView = findViewById(R.id.text_intro);
        bankTextView = findViewById(R.id.text_bank);
        layoutShopname = findViewById(R.id.layout_shopname);
        layoutEmail = findViewById(R.id.layout_email);
        layoutPhone = findViewById(R.id.layout_phone);
        layoutAddress = findViewById(R.id.layout_address);
        layoutSns = findViewById(R.id.layout_sns);
        layoutIntro = findViewById(R.id.layout_intro);

        // Intent에서 shop_id 받아오기
        int shopId = getIntent().getIntExtra("SHOP_ID", -1);
        if (shopId != -1) {
            fetchShopId(shopId); // shop_id로 가게 정보 가져오기
        } else {
            Toast.makeText(this, "유효한 가게 ID가 아닙니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void fetchShopId(int shopId) {
        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("AccessToken", null);

        if (accessToken != null) {
            Retrofit retrofit = RetrofitClient.getClient(accessToken);
            LoginApiService apiService = retrofit.create(LoginApiService.class);
            Call<StoreInfoResponse> call = apiService.getStorename("Bearer " + accessToken, shopId);

            call.enqueue(new Callback<StoreInfoResponse>() {
                @Override
                public void onResponse(Call<StoreInfoResponse> call, Response<StoreInfoResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        StoreInfoResponse userInfo = response.body();
                        shopnameTextView.setText(userInfo.getShopname());
                        phoneTextView.setText(userInfo.getPhone());
                        emailTextView.setText(userInfo.getEmail());
                        addressTextView.setText(userInfo.getAddress());
                        snsTextView.setText(userInfo.getSns());
                        introTextView.setText(userInfo.getIntro());
                        bankTextView.setText(userInfo.getBank());

                        // 주소가 없는 경우 레이아웃 숨기기
                        if (userInfo.getAddress() == null || userInfo.getAddress().isEmpty()) {
                            layoutAddress.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(user_shop_home.this, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<StoreInfoResponse> call, Throwable t) {
                    Toast.makeText(user_shop_home.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "로그인 토큰이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}