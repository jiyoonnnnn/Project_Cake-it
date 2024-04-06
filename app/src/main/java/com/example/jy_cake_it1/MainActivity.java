package com.example.jy_cake_it1;

import android.app.Application;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.jy_cake_it1.databinding.ActivityMainBinding;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

//public interface DataService {
//    @GET("/shop/")
//    Call<List<JSONObject>> getList
//}
//public interface ApiService {
//    @GET("/")
//    Call<ResponseBody> getRoot();
//
//    @POST("/items/")
//    Call<ResponseBody> createItem(@Body RequestBody requestBody);
//}
//    ApiService apiService = retrofit.create(ApiService.class);
//public class configs extends Application {
//    private String Url = "http://132.145.80.50:8888/";
//    private String strPhone = "";
//}

public class MainActivity extends AppCompatActivity {
    public interface ApiService {
        @GET("/")
        Call<ResponseBody> getRoot();

        @POST("/shop/")
        Call<ResponseBody> createShop(@Body RequestBody requestBody);
    }
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://132.145.80.50:8888/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ApiService apiService = retrofit.create(ApiService.class);
    Call<ResponseBody> call = apiService.getRoot();
    call.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            // 응답 처리
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            // 에러 처리
        }
    });
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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