package com.example.jy_cake_it2.JY;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jy_cake_it2.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_browse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_browse);

        TextView shopList;
        shopList = findViewById(R.id.browse);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://132.145.80.50:9999/") // 기본 URL만 입력
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginApiService service = retrofit.create(LoginApiService.class);

//        Call<List<Shop>> call = service.getShops();
//        call.enqueue(new Callback<List<Shop>>() {
//            @Override
//            public void onResponse(Call<List<Shop>> call, Response<List<Shop>> response) {
//                if (response.isSuccessful()) {
//                    List<Shop> shops = response.body();
//
//                    for (Shop shop : shops) {
//
//                        String content = "";
//                        content += "번호: " + shop.getPhone() + "\n";
//                        content += "상호명: " + shop.getStoreName() + "\n";
//                        content += "주소: " + shop.getStoreAddress() + "\n\n";
//                        dataTextView3.append(content);
//                    }
//                } else {
//                    System.out.println("Failed to fetch shops: " + response.message());
//                }
//            }
//            @Override
//            public void onFailure(Call<List<Shop>> call, Throwable t) {
//                System.out.println("Failed to fetch shops: " + t.getMessage());
//            }
//        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}