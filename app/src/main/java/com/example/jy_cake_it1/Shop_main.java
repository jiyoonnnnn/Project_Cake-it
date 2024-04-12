package com.example.jy_cake_it1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.jy_cake_it1.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Shop_main extends AppCompatActivity {
    private ActivityMainBinding binding;

    private TextView dataTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
                        System.out.println("Name: " + shop.getStoreName());
                        dataTextView.setText("상호명: " + shop.getStoreName());
                        System.out.println("Phone: " + shop.getPhone());
                        System.out.println("Address: " + shop.getStoreAddress());
                        System.out.println("Number: " + shop.getId());
                        System.out.println();
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
        dataTextView = findViewById(R.id.dataTextView);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}
