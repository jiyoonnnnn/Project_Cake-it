package com.example.jy_cake_it2.JY;

import android.graphics.Color;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.jy_cake_it2.R;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_browse extends AppCompatActivity {
    private final int Fragment_1 = 1;
    private final int Fragment_2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_browse);
        TextView shoplist = findViewById(R.id.shoplist);
        TextView designlist = findViewById(R.id.designlist);
        NavigationView navigationView;
        Fragment shop_list, design_list;
        findViewById(R.id.shoplist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoplist.setTextColor(Color.parseColor("#000000"));
                designlist.setTextColor(Color.parseColor("#FFFFFF"));
                출처: https://kanzler.tistory.com/248 [kanzler의 세상 이야기:티스토리]
                FragmentView(Fragment_1);

            }
        });

        findViewById(R.id.designlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoplist.setTextColor(Color.parseColor("#FFFFFF"));
                designlist.setTextColor(Color.parseColor("#000000"));
                FragmentView(Fragment_2);


            }
        });
        FragmentView(Fragment_1);

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://132.145.80.50:9999/") // 기본 URL만 입력
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        LoginApiService service = retrofit.create(LoginApiService.class);
//
//        Call<ApiResponse> call = service.getShops();
//        call.enqueue(new Callback<ApiResponse>() {
//            @Override
//            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
//                shopList3.setText("code = " + response.code());
//                if (response.isSuccessful()) {
////                    shopList3.setText(response.body());
//
//                    ApiResponse apiResponse = response.body();
//                    List<Shop> shops = apiResponse.getShop_list();
//                    for (Shop shop : shops) {
////                        shopList3.setText(shop.toString());
//
//
////                            shopList3.setText("번호: " + shops.getShop_list() + "\n");
//
//                        String content = "";
//                        content += "번호: " + shop.getId() + "\n";
//                        content += "상호명: " + shop.getShopname() + "\n";
//                        content += "주소: " + shop.getAddress() + "\n";
//                        content += "사장님: " + shop.getUsername() + "\n"+"\n";
//
//
//                        shopList2.append(content);
//
//                    }
//                } else {
//                    Toast.makeText(activity_browse.this, "망해따", Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<ApiResponse> call, Throwable t) {
//                Toast.makeText(activity_browse.this, "Failed to fetch shops", Toast.LENGTH_SHORT).show();
//                shopList3.setText(t.getMessage());
//            }
//        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void FragmentView(int fragment){

        //FragmentTransactiom를 이용해 프래그먼트를 사용합니다.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment){
            case 1:
                // 첫번 째 프래그먼트 호출
                Shop_list fragment1 = new Shop_list();
                transaction.replace(R.id.fragment_container, fragment1);
                transaction.commit();
                break;

            case 2:
                // 두번 째 프래그먼트 호출
                Design_list fragment2 = new Design_list();
                transaction.replace(R.id.fragment_container, fragment2);
                transaction.commit();
                break;
        }

    }
}