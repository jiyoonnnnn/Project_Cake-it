package com.example.jy_cake_it2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Shop_main extends AppCompatActivity {
    private TextView dataTextView, dataTextView2, dataTextView3;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://132.145.80.50:8888/") // 기본 URL만 입력
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopApiService service = retrofit.create(ShopApiService.class); // ShopService 대신 ShopApiService 사용

        Call<List<Shop>> call = service.getShops();
        call.enqueue(new Callback<List<Shop>>() {
            @Override
            public void onResponse(Call<List<Shop>> call, Response<List<Shop>> response) {
                if (response.isSuccessful()) {
                    List<Shop> shops = response.body();

                    for (Shop shop : shops) {
                        if (shop.getId()==8){
                            dataTextView.setText("번호: " + shop.getPhone());
                            dataTextView2.setText("상호명: " + shop.getStoreName());
                        }
                        String content = "";
                        content += "번호: " + shop.getPhone() + "\n";
                        content += "상호명: " + shop.getStoreName() + "\n";
                        content += "주소: " + shop.getStoreAddress() + "\n\n";
                        dataTextView3.append(content);
                    }
                } else {
                    System.out.println("Failed to fetch shops: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Shop>> call, Throwable t) {
                System.out.println("Failed to fetch shops: " + t.getMessage());
            }
        });
//        Shop shop = Shop.create(MediaType.parse("application/json"), "{\"key\": \"value\"}");
        Shop shop = new Shop("2312-23-32423", "박지윤케이크", "중앙로13");
        Call<Shop> postCall = service.setShop(shop);
//        postCall.enqueue(new Callback<Shop>() {
//            @Override
//            public void onResponse(Call<Shop> call, Response<Shop> response) {
//                // 응답 처리
//                if (response.isSuccessful())
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                // 에러 처리
//            }
//        });
        dataTextView = findViewById(R.id.dataTextView);
        dataTextView2 = findViewById(R.id.dataTextView2);
        dataTextView3 = findViewById(R.id.dataTextView3);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
