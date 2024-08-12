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


public class Login_main extends AppCompatActivity {
//    private SharedPreferences sharedPreferences;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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
                Call<ApiResponse> call = service.login(user, pass);
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        Toast.makeText(Login_main.this, "code: " + response.code(), Toast.LENGTH_LONG).show();
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
                                Toast.makeText(Login_main.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login_main.this, activity_draw_cake.class);
                                startActivity(intent);
                            } else {
                                // `apiResponse`가 `null`인 경우 처리
                                Toast.makeText(Login_main.this, "response_body is null", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // 응답이 실패한 경우 처리
                            Toast.makeText(Login_main.this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        // 네트워크 요청 실패 처리
                        Toast.makeText(Login_main.this, "Network error.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



//
//        Login data = new Login("케이크 만월", "청주시 상당구 상당로", "02-12345");
//        Call<ResponseBody> postCall = service.createPost(data);
//        postCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                // 응답 처리
//                dataTextView.setText("code: " + response.code());
//                if (response.isSuccessful()){
//                    String content = "";
//                    content += "상호명: " + data.getStoreName() + "\n";
//                    content += "주소: " + data.getStoreAddress() + "\n";
//                    content += "연락처: " + data.getPhone() + "\n\n";
//                    dataTextView3.append(content);
//                }
//                else if (response.code() == 307) {
//                    // 임시 리디렉션 응답을 받은 경우, 새로운 위치로 재시도
//                    String newLocation = response.raw().header("Location");
//                    if (newLocation != null) {
//                        // 새로운 위치로 재시도
//                        // newLocation에 있는 URL로 다시 요청을 보내야 합니다.
//                        dataTextView.setText("Temporary redirect to: " + newLocation);
//                    } else {
//                        // 새로운 위치가 제공되지 않은 경우에 대한 처리
//                        dataTextView.setText("Temporary redirect, but no new location provided");
//                    }
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> postCall, Throwable t) {
//                t.printStackTrace();
//                dataTextView2.setText(t.getMessage());
//                // 요청이 실패한 경우 처리하는 코드 작성
//            }
//        });
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
                Intent intent = new Intent(Login_main.this, activity_signup.class);
                startActivity(intent);
            }
        });
        Button btn2;
        btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_main.this, activity_shopsignup.class);
                startActivity(intent);
            }
        });
        Button btn3;
        btn3 = findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_main.this, LoginShop.class);
                startActivity(intent);
            }
        });

    }
}

