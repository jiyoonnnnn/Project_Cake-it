package com.example.jy_cake_it2.JY;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jy_cake_it2.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_browse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_browse);

        TextView shopList,shopList2,shopList3;
        shopList = findViewById(R.id.browse);
        shopList2 = findViewById(R.id.browse2);
        shopList3 = findViewById(R.id.browse3);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://132.145.80.50:9999/") // 기본 URL만 입력
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginApiService service = retrofit.create(LoginApiService.class);

        Call<ApiResponse> call = service.getShops();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                shopList2.setText("code = " + response.code());
                if (response.isSuccessful()) {
//                    shopList3.setText(response.body());

                    ApiResponse apiResponse = response.body();
                    List<Shop> shops = apiResponse.getShop_list();
                    for (Shop shop : shops) {
//                        shopList3.setText(shop.toString());


//                            shopList3.setText("번호: " + shops.getShop_list() + "\n");

                        String content = "";
                        content += "번호: " + shop.getId() + "\n";
                        content += "상호명: " + shop.getShopname() + "\n";
                        content += "사장님: " + shop.getUsername() + "\n";


                        shopList.append(content);

                    }
                } else {
                    Toast.makeText(activity_browse.this, "망해따", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(activity_browse.this, "Failed to fetch shops", Toast.LENGTH_SHORT).show();
                shopList3.setText(t.getMessage());
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}