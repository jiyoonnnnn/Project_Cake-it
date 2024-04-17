package com.example.jy_cake_it2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
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
        //        Shop shop = Shop.create(MediaType.parse("application/json"), "{\"key\": \"value\"}");
        Shop data = new Shop("2312-23-32423", "박지윤케이크", "중앙로13");
        Call<ResponseBody> postCall = service.createPost(data);
        postCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // 응답 처리
                dataTextView.setText("code: " + response.code());
                if (response.isSuccessful()){
                    dataTextView.setText("code: " + response.code());
                    dataTextView2.setText("Name: " + data.getStoreName());
                    try {
                        String responseData = response.body().string();
                        dataTextView3.setText("Response: " + responseData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (response.code() == 307) {
                    // 임시 리디렉션 응답을 받은 경우, 새로운 위치로 재시도
                    String newLocation = response.raw().header("Location");
                    if (newLocation != null) {
                        // 새로운 위치로 재시도
                        // newLocation에 있는 URL로 다시 요청을 보내야 합니다.
                        dataTextView.setText("Temporary redirect to: " + newLocation);
                    } else {
                        // 새로운 위치가 제공되지 않은 경우에 대한 처리
                        dataTextView.setText("Temporary redirect, but no new location provided");
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> postCall, Throwable t) {
                t.printStackTrace();
                dataTextView2.setText(t.getMessage());
                dataTextView3.setText("Response: ");
                // 요청이 실패한 경우 처리하는 코드 작성
            }
        });
//        Call<List<Shop>> call = service.getShops();
//        call.enqueue(new Callback<List<Shop>>() {
//            @Override
//            public void onResponse(Call<List<Shop>> call, Response<List<Shop>> response) {
//                if (response.isSuccessful()) {
//                    List<Shop> shops = response.body();
//
//                    for (Shop shop : shops) {
//                        if (shop.getId()==8){
//                            dataTextView.setText("번호: " + shop.getPhone() + "\n");
//                        }
////                        String content = "";
////                        content += "번호: " + shop.getPhone() + "\n";
////                        content += "상호명: " + shop.getStoreName() + "\n";
////                        content += "주소: " + shop.getStoreAddress() + "\n\n";
////                        dataTextView3.append(content);
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


            dataTextView = findViewById(R.id.dataTextView);
            dataTextView2 = findViewById(R.id.dataTextView2);
            dataTextView3 = findViewById(R.id.dataTextView3);

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
//        Button btn1;
//        btn1 = findViewById(R.id.button1);
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Shop_main.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });

    }
}

