package com.example.jy_cake_it1;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.jy_cake_it1.databinding.ActivityMainBinding;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

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

    public class Shop {
        private String phone;
        private int num;
        private String name;
        private String address;

        public String getPhone() {
            return name;
        }
        public void setPhone(String phone) {
            this.phone = phone;
        }
        public int getNum() {
            return num;
        }
        public void setNum(int num) {
            this.num = num;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getAddress() {
            return address;
        }
        public void setAddress(String address) {
            this.address = address;
        }
    }
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://132.145.80.50:8888/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ApiService apiService = retrofit.create(ApiService.class);
    Call<Shop> call = apiService.getName();
    call.enqueue(new Callback<Shop>() {
        @Override
        public void onResponse(Call<Shop> call, Response<Shop> response) {
            // 응답 처리
            if (response.isSuccessful()) {
                Shop data = response.body();
                // 데이터를 화면에 표시하는 메서드 호출
                displayData(data);
            } else {
                // 에러 처리
                showError();
            }
        }

        @Override
        public void onFailure(Call<List<Shop>> call, Throwable t) {
            // 에러 처리
        }
    });
    private void displayData(Shop data) {
        // 데이터를 사용하여 UI를 업데이트합니다.
        dataTextView.setText(data.getName());
        // 예시: 데이터 모델의 getTitle() 메서드를 사용하여 타이틀을 표시하는 TextView에 데이터를 설정합니다.
    }
    // 에러 처리
    private void showError() {
        // 에러 메시지를 표시하는 등의 작업을 수행합니다.
        Toast.makeText(getApplicationContext(), "네트워크 요청 실패", Toast.LENGTH_SHORT).show();
    }
//    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"key\": \"value\"}");
//    Call<Shop> postCall = apiService.createShop(requestBody);
//postCall.enqueue(new Callback<ResponseBody>() {
//        @Override
//        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//            // 응답 처리
//        }
//
//        @Override
//        public void onFailure(Call<ResponseBody> call, Throwable t) {
//            // 에러 처리
//        }
//    });
    private ActivityMainBinding binding;

    private TextView dataTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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