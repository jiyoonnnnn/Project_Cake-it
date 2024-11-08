package com.example.jy_cake_it2.JY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jy_cake_it2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Activity_my_page extends AppCompatActivity {
    private ImageView userPic;
    private TextView title_myPage,  id, email, phoneNum, text_id, text_email, text_phoneNum;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        //가능하면 회원가입 단계에서 사진을 추가해서 넣어줄 수 있게 해주면 좋다.
        userPic = findViewById(R.id.user_pic);

        title_myPage = findViewById(R.id.title_myPage);

        id = findViewById(R.id.myPage_id);
        email = findViewById(R.id.myPage_email);
        phoneNum = findViewById(R.id.myPage_phoneNum);

        text_id = findViewById(R.id.text_id);
        text_email = findViewById(R.id.text_email);
        text_phoneNum = findViewById(R.id.text_phoneNum);


        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("AccessToken", null);
        if (accessToken != null) {
            Retrofit retrofit = RetrofitClient.getClient(accessToken);
            LoginApiService apiService = retrofit.create(LoginApiService.class);
            Call<UserInfoResponse> call = apiService.getUserInfo("Bearer " + accessToken);

            call.enqueue(new Callback<UserInfoResponse>() {
                @Override
                public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                    Toast.makeText(Activity_my_page.this, "code: " + response.code(), Toast.LENGTH_LONG).show();
                    if (response.isSuccessful() && response.body() != null) {
                        UserInfoResponse userInfo = response.body();
                        text_id.setText(userInfo.getUsername());
                        text_phoneNum.setText(userInfo.getPhone());
                        text_email.setText(userInfo.getEmail());


                    } else {
                        Toast.makeText(Activity_my_page.this, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                    Toast.makeText(Activity_my_page.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_mypage);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_draw_cake) {
//                switch (item.getItemId()) {
//                    case R.id.nav_draw_cake:
                    startActivity(new Intent(Activity_my_page.this, activity_draw_cake.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_browse) {
//                    case R.id.nav_browse:
                    startActivity(new Intent(Activity_my_page.this, activity_browse.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_order) {
//                    case R.id.nav_order:
                    startActivity(new Intent(Activity_my_page.this, user_order.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_mypage) {
//                    case R.id.nav_mypage:
                    startActivity(new Intent(Activity_my_page.this, Activity_my_page.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

    }
}