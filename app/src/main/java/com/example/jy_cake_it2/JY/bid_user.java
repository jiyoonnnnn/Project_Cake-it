package com.example.jy_cake_it2.JY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jy_cake_it2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class bid_user extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Detail> questionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_user);

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 어댑터 초기화 - 빈 리스트로 설정
        orderAdapter = new OrderAdapter(new ArrayList<>(), new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Detail detail) {
                // 클릭 시 주문 세부사항으로 이동
                Intent intent = new Intent(bid_user.this, Bid_user_detail.class);
                intent.putExtra("ORDER_ID", detail.getId());
                startActivity(intent);
            }
        });
        TextView btn1;
        btn1 = findViewById(R.id.back);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bid_user.this, activity_browse.class);
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
                    Toast.makeText(bid_user.this, "code: " + response.code(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(bid_user.this, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(bid_user.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
