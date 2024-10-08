package com.example.jy_cake_it2.JY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class shop_order extends AppCompatActivity {
    private android.widget.TextView TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shop_order);
        TextView btn1;
        btn1 = findViewById(R.id.back);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(shop_order.this, LoginShop.class);
                startActivity(intent);
            }
        });

        TextView = findViewById(R.id.textView);

        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("AccessToken", null);

        if (accessToken != null) {
            Retrofit retrofit = RetrofitClient.getClient(accessToken);
            LoginApiService apiService = retrofit.create(LoginApiService.class);
            Call<ApiResponse> call = apiService.getShopOrders("Bearer " + accessToken);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        ApiResponse apiResponse = response.body();
                        List<Detail> orders = apiResponse.getOrder_list();
                        for (Detail detail : orders) {
                            String content = "";
                            content += "\n"+ "제목 : " + detail.getSubject() + "\n";
                            content += "내용 : " + detail.getContent() + "\n";
                            content += "픽업일 : " + detail.getCreateDate() + "\n";
                            TextView.append(content);
                        }

//                        Intent intent = new Intent(user_order.this, bid_user.class);
//                        // 다른 페이지로 전달할 데이터가 있다면 여기에 추가
//                        startActivity(intent);
                    } else {
                        Toast.makeText(shop_order.this, "Failed to fetch shops", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(shop_order.this, "Error fetching shops: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_shopPage) {
                    startActivity(new Intent(shop_order.this, bids_shop.class));
                    return true;
                } else if (id == R.id.nav_bid_list) {
                    startActivity(new Intent(shop_order.this, bids_shop.class));
                    return true;
                } else if (id == R.id.nav_order) {
                    startActivity(new Intent(shop_order.this, shop_order.class));
                    return true;
                } else if (id == R.id.nav_mypage) {
                    startActivity(new Intent(shop_order.this, bids_shop.class));
                    return true;
                }
                return false;
            }
        });
    }
}