package com.example.jy_cake_it2.JY;

import android.content.Intent;
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

public class bids_shop extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Detail> questionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bids_shop);


        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 어댑터 초기화 - 빈 리스트로 설정
        orderAdapter = new OrderAdapter(new ArrayList<>(), new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Detail detail) {
                // 클릭 시 주문 세부사항으로 이동
                Intent intent = new Intent(bids_shop.this, Bid_shop_detail.class);
                intent.putExtra("ORDER_ID", detail.getId());
                startActivity(intent);
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        ViewCompat.setOnApplyWindowInsetsListener(bottomNavigationView, (view, insets) -> {
            // 하단 네비게이션 바 높이만큼 패딩 적용
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            bottomNavigationView.setPadding(0, 0, 0, bottomInset);
            return insets;
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_bid_list);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_shopPage) {
                    startActivity(new Intent(bids_shop.this, Shop_page.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_bid_list) {
                    startActivity(new Intent(bids_shop.this, bids_shop.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_order) {
                    startActivity(new Intent(bids_shop.this, shop_order.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_mypage) {
                    startActivity(new Intent(bids_shop.this, Activity_my_page_store.class));
                    finish();
                    return true;
                }
                return false;
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
    }
    private void fetchQuestions() {
        LoginApiService apiService = RetrofitClient.getApiService();
        Call<ApiResponse> call = apiService.getOrderList();

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Toast.makeText(bids_shop.this, "code: " + response.code(), Toast.LENGTH_LONG).show();
                if (response.isSuccessful() ) {
                    ApiResponse questionList = response.body();
                    List<Detail> orders = questionList.getOrder_list();
                    orderAdapter.updateOrders(orders);

                } else {
                    Toast.makeText(bids_shop.this, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(bids_shop.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}