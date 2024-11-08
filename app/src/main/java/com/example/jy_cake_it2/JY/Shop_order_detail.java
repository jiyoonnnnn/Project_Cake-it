package com.example.jy_cake_it2.JY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jy_cake_it2.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Shop_order_detail extends AppCompatActivity {
    private TextView idTextView;
    private TextView subjectTextView;
    private TextView contentTextView;
    private TextView createDateTextView;
    private TextView userTextView;
    private TextView modifyDateTextView;
    private TextView typeTextView;
    private TextView shapeTextView;
    private TextView colorTextView;
    private TextView flavorTextView;
    private TextView pickupDateTextView;
    private TextView letteringTextView, statusTextView;
    private int question_id, statusCode;;
    ImageView imageView;
    Button btn_accept, btn_deny, btn_wait, btn_finish, btn_end;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shop_order_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //idTextView = findViewById(R.id.idTextView);
        //subjectTextView = findViewById(R.id.subjectTextView);
        contentTextView = findViewById(R.id.contentTextView);
        createDateTextView = findViewById(R.id.createDateTextView);
        userTextView = findViewById(R.id.userTextView);
        typeTextView = findViewById(R.id.typeTextView);
        shapeTextView = findViewById(R.id.shapeTextView);
        colorTextView = findViewById(R.id.colorTextView);
        flavorTextView = findViewById(R.id.flavorTextView);
        pickupDateTextView = findViewById(R.id.pickupDateTextView);
        statusTextView = findViewById(R.id.statusTextView);
        letteringTextView = findViewById(R.id.letteringTextView);
        btn_accept = findViewById(R.id.btn_accept);
        btn_deny = findViewById(R.id.btn_deny);
        btn_wait = findViewById(R.id.btn_wait);
        btn_finish = findViewById(R.id.btn_finish);
        btn_end = findViewById(R.id.btn_end);
        imageView = findViewById(R.id.cake_image);
        Intent intent = getIntent();
        question_id = intent.getIntExtra("ORDER_ID", -1);

        fetchOrderDetail(question_id);
    }

    private void fetchOrderDetail(int question_id) {
        Gson gson = new GsonBuilder()
                .setLenient() // This allows lenient parsing of JSON
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://132.145.80.50:9999/") // 실제 API의 베이스 URL로 변경하세요
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        LoginApiService apiService = retrofit.create(LoginApiService.class);

        Call<Detail> call = apiService.getDetail(question_id);
        call.enqueue(new Callback<Detail>() {
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Detail orderDetail = response.body();
                    statusCode = orderDetail.getOrderStatus();
                    //subjectTextView.setText("subject : " + orderDetail.getSubject());
                    contentTextView.setText("요청사항 : " + orderDetail.getContent());
                    createDateTextView.setText("주문 생성 날짜 : " + orderDetail.getCreateDate());
                    userTextView.setText("주문자 이름 : " + orderDetail.getUser().getUsername());
                    typeTextView.setText("케이크 크기 : " + orderDetail.getCakeType());
                    shapeTextView.setText("케이크 모양 : " + orderDetail.getCakeShape());
                    colorTextView.setText("케이크 색 : " + orderDetail.getCakeColor());
                    flavorTextView.setText("맛 : " + orderDetail.getCakeFlavor());
                    pickupDateTextView.setText("픽업 날짜 : " + orderDetail.getPickupDate());
                    letteringTextView.setText("레터링 : " + orderDetail.getLettering());
                    fetchCakeImage(orderDetail.getCakeIMG());
                    setButtonVisibility(statusCode);
                } else if (response.code() == 307) {
                    // 임시 리디렉션 응답을 받은 경우, 새로운 위치로 재시도
                    String newLocation = response.raw().header("Location");
                    if (newLocation != null) {
                        // 새로운 위치로 재시도
                        // newLocation에 있는 URL로 다시 요청을 보내야 합니다.
                        subjectTextView.setText("Temporary redirect to: " + newLocation);
                    } else {
                        // 새로운 위치가 제공되지 않은 경우에 대한 처리
                        subjectTextView.setText("Temporary redirect, but no new location provided");
                    }
                }
            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                Toast.makeText( Shop_order_detail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("AccessToken", null);
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//상태를 30으로변경해야함 코드는 bid_user_detail에 있음

                if (accessToken != null) {
                    Retrofit retrofit = RetrofitClient.getClient(accessToken);
                    LoginApiService apiService = retrofit.create(LoginApiService.class);
                    OrderRequest orderRequest = new OrderRequest(question_id);
                    Call<Detail> call = apiService.orderAccept("Bearer " + accessToken, orderRequest);

                    call.enqueue(new Callback<Detail>() {
                        @Override
                        public void onResponse(Call<Detail> call, Response<Detail> response) {
                            Toast.makeText(Shop_order_detail.this, "code: " + response.code(), Toast.LENGTH_LONG).show();
                            if (response.isSuccessful() ) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(Shop_order_detail.this, "주문수락! ", Toast.LENGTH_LONG).show();
                                } else if (response.code() == 400) {
                                    // 임시 리디렉션 응답을 받은 경우, 새로운 위치로 재시도
                                    Toast.makeText(Shop_order_detail.this, "오류 ", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(Shop_order_detail.this, "완전오류 ", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Detail> call, Throwable t) {
                            Toast.makeText(Shop_order_detail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        btn_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//상태를 30으로변경해야함 코드는 bid_user_detail에 있음

                if (accessToken != null) {
                    Retrofit retrofit = RetrofitClient.getClient(accessToken);
                    LoginApiService apiService = retrofit.create(LoginApiService.class);
                    DenyRequest denyRequest = new DenyRequest(question_id,"영업안해요");
                    Call<Detail> call = apiService.orderDeny("Bearer " + accessToken, denyRequest);

                    call.enqueue(new Callback<Detail>() {
                        @Override
                        public void onResponse(Call<Detail> call, Response<Detail> response) {
                            Toast.makeText(Shop_order_detail.this, "code: " + response.code(), Toast.LENGTH_LONG).show();
                            if (response.isSuccessful() ) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(Shop_order_detail.this, "주문거절! ", Toast.LENGTH_LONG).show();
                                } else if (response.code() == 400) {
                                    // 임시 리디렉션 응답을 받은 경우, 새로운 위치로 재시도
                                    Toast.makeText(Shop_order_detail.this, "오류 ", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(Shop_order_detail.this, "완전오류 ", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Detail> call, Throwable t) {
                            Toast.makeText(Shop_order_detail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        btn_wait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (accessToken != null) {
                    Retrofit retrofit = RetrofitClient.getClient(accessToken);
                    LoginApiService apiService = retrofit.create(LoginApiService.class);
                    ChangeStatus changeStatus1 = new ChangeStatus(question_id, 30);
                    Call<ChangeStatus> call = apiService.changeStatus("Bearer " + accessToken, changeStatus1);

                    call.enqueue(new Callback<ChangeStatus>() {
                        @Override
                        public void onResponse(Call<ChangeStatus> call, Response<ChangeStatus> response) {
                            Toast.makeText(Shop_order_detail.this, "code: " + response.code(), Toast.LENGTH_LONG).show();
                            if (response.isSuccessful()) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(Shop_order_detail.this, "케이크 제작중으로 변경! ", Toast.LENGTH_LONG).show();
                                } else if (response.code() == 400) {
                                    // 임시 리디렉션 응답을 받은 경우, 새로운 위치로 재시도
                                    Toast.makeText(Shop_order_detail.this, "오류 ", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(Shop_order_detail.this, "완전오류 ", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ChangeStatus> call, Throwable t) {
                            Toast.makeText(Shop_order_detail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//상태를 30으로변경해야함 코드는 bid_user_detail에 있음

                if (accessToken != null) {
                    Retrofit retrofit = RetrofitClient.getClient(accessToken);
                    LoginApiService apiService = retrofit.create(LoginApiService.class);
                    ChangeStatus changeStatus2 = new ChangeStatus(question_id, 31);
                    Call<ChangeStatus> call = apiService.changeStatus("Bearer " + accessToken, changeStatus2);

                    call.enqueue(new Callback<ChangeStatus>() {
                        @Override
                        public void onResponse(Call<ChangeStatus> call, Response<ChangeStatus> response) {
                            Toast.makeText(Shop_order_detail.this, "code: " + response.code(), Toast.LENGTH_LONG).show();
                            if (response.isSuccessful()) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(Shop_order_detail.this, "제작완료로 변경! ", Toast.LENGTH_LONG).show();
                                } else if (response.code() == 400) {
                                    // 임시 리디렉션 응답을 받은 경우, 새로운 위치로 재시도
                                    Toast.makeText(Shop_order_detail.this, "오류 ", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(Shop_order_detail.this, "완전오류 ", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ChangeStatus> call, Throwable t) {
                            Toast.makeText(Shop_order_detail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//상태를 30으로변경해야함 코드는 bid_user_detail에 있음

                if (accessToken != null) {
                    Retrofit retrofit = RetrofitClient.getClient(accessToken);
                    LoginApiService apiService = retrofit.create(LoginApiService.class);
                    ChangeStatus changeStatus1 = new ChangeStatus(question_id, 32);
                    Call<ChangeStatus> call = apiService.changeStatus("Bearer " + accessToken, changeStatus1);

                    call.enqueue(new Callback<ChangeStatus>() {
                        @Override
                        public void onResponse(Call<ChangeStatus> call, Response<ChangeStatus> response) {
                            Toast.makeText(Shop_order_detail.this, "code: " + response.code(), Toast.LENGTH_LONG).show();
                            if (response.isSuccessful()) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(Shop_order_detail.this, "픽업완료로 변경! ", Toast.LENGTH_LONG).show();
                                } else if (response.code() == 400) {
                                    // 임시 리디렉션 응답을 받은 경우, 새로운 위치로 재시도
                                    Toast.makeText(Shop_order_detail.this, "오류 ", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(Shop_order_detail.this, "완전오류 ", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ChangeStatus> call, Throwable t) {
                            Toast.makeText(Shop_order_detail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    private void fetchCakeImage(String imageFileName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://132.145.80.50:9999/")
                .build();

        LoginApiService apiService = retrofit.create(LoginApiService.class);

        Call<ResponseBody> imageCall = apiService.getImage(imageFileName);
        imageCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 비트맵으로 변환
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    //Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    imageView.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(Shop_order_detail.this, "이미지를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Shop_order_detail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setButtonVisibility(int statusCode) {
        if (statusCode == 10) {
            btn_accept.setVisibility(View.VISIBLE);
            btn_deny.setVisibility(View.VISIBLE);
            btn_wait.setVisibility(View.GONE);
            btn_finish.setVisibility(View.GONE);
            btn_end.setVisibility(View.GONE);
        } else {
            btn_accept.setVisibility(View.GONE);
            btn_deny.setVisibility(View.GONE);
            btn_wait.setVisibility(View.VISIBLE);
            btn_finish.setVisibility(View.VISIBLE);
            btn_end.setVisibility(View.VISIBLE);
        }
    }
}