package com.example.jy_cake_it2.JY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jy_cake_it2.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginShop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_shop);

        EditText username = findViewById(R.id.editID);
        EditText password = findViewById(R.id.editPw);
        Button loginBtn = findViewById(R.id.loginbutton);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://132.145.80.50:9999/") // 기본 URL만 입력
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginApiService service = retrofit.create(LoginApiService.class);
//        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
//                            억세스토큰은, 이제 앱에서 api 호출할 때마다 헤더에 넣어서 보내야 한다.
//                            따라서 억세스토큰은, 쉐어드 프리퍼런스에 저장해 놓는다.
        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 입력 데이터 가져오기
                String user = username.getText().toString();
                String pass = password.getText().toString();

                // 로그인 요청 데이터 생성

                // 로그인 요청 보내기
                Call<ApiResponse> call = service.shoplogin(user, pass);
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        Toast.makeText(LoginShop.this, "code: " + response.code(), Toast.LENGTH_LONG).show();
                        if (response.isSuccessful()) {
                            ApiResponse apiResponse = response.body();
                            if (apiResponse != null) {
                                String accessToken = apiResponse.getAccessToken();
                                String tokenType = apiResponse.getTokenType();
                                String username = apiResponse.getUsername();
                                SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("AccessToken", accessToken); // 토큰 저장
                                editor.putString("TokenType", tokenType);
                                editor.putString("Username", username);
                                editor.apply(); // 변경사항 적용
                                Toast.makeText(LoginShop.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginShop.this, activity_browse.class);
                                startActivity(intent);
                            } else {
                                // `apiResponse`가 `null`인 경우 처리
                                Toast.makeText(LoginShop.this, "response_body is null", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // 응답이 실패한 경우 처리
                            Toast.makeText(LoginShop.this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        // 네트워크 요청 실패 처리
                        Toast.makeText(LoginShop.this, "Network error.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btn1;
        btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginShop.this, activity_shopsignup.class);
                startActivity(intent);
            }
        });
        Button btn2;
        btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginShop.this, activity_signup.class);
                startActivity(intent);
            }
        });
        Button btn3;
        btn3 = findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginShop.this, Login_main.class);
                startActivity(intent);
            }
        });

    }
}