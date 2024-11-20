package com.example.jy_cake_it2.JY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jy_cake_it2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class user_order extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Detail> questionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_order);

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 어댑터 초기화 - 빈 리스트로 설정
        orderAdapter = new OrderAdapter(new ArrayList<>(), new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Detail detail) {
                // 클릭 시 주문 세부사항으로 이동
                Intent intent = new Intent(user_order.this, user_order_detail.class);
                intent.putExtra("ORDER_ID", detail.getId());
                startActivity(intent);
            }
        });
//        orderAdapter = new OrderAdapter(new ArrayList<>(), new OrderAdapter.OnItemClickListener());
        recyclerView.setAdapter(orderAdapter);
        fetchQuestions();
        // Sample data
//        questionList = new ArrayList<>();
//
//        // Set up adapter
//        questionAdapter = new OrderAdapter(questionList, new OrderAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Detail detail) {
//                // Handle item click
//                // 예를 들어, 클릭 시 주문 세부사항 화면으로 이동
//                 Intent intent = new Intent(bids_shop.this, Bid_shop_detail.class);
//                 intent.putExtra("ORDER_ID", detail.getId());
//                 startActivity(intent);
//            }
//        });
//
//        recyclerView.setAdapter(questionAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_order);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_draw_cake) {
//                switch (item.getItemId()) {
//                    case R.id.nav_draw_cake:
                    startActivity(new Intent(user_order.this, activity_draw_cake.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_browse) {
//                    case R.id.nav_browse:
                    startActivity(new Intent(user_order.this, activity_browse.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_order) {
//                    case R.id.nav_order:
                    startActivity(new Intent(user_order.this, user_order.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_mypage) {
//                    case R.id.nav_mypage:
                    startActivity(new Intent(user_order.this, Activity_my_page.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
    private void fetchQuestions() {
        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("AccessToken", null);

        if (accessToken != null) {
            Retrofit retrofit = RetrofitClient.getClient(accessToken);
            LoginApiService apiService = retrofit.create(LoginApiService.class);
            Call<ApiResponse> call = apiService.getUserOrders("Bearer " + accessToken);

            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    Toast.makeText(user_order.this, "code: " + response.code(), Toast.LENGTH_LONG).show();
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse questionList = response.body();
                        List<Detail> orders = questionList.getOrder_list();
                        orderAdapter.updateOrders(orders);
                        // RecyclerView에 데이터 설정
//                    orderAdapter = new OrderAdapter(orders, new OrderAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(Detail detail) {
//                            // 클릭 시 처리
//                            // 예를 들어 주문 세부사항으로 이동
//                             Intent intent = new Intent(bids_shop.this, Bid_shop_detail.class);
//                             intent.putExtra("ORDER_ID", detail.getId());
//                             startActivity(intent);
//                        }
//                    });
//                    recyclerView.setAdapter(orderAdapter);
                    } else {
                        Toast.makeText(user_order.this, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(user_order.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}