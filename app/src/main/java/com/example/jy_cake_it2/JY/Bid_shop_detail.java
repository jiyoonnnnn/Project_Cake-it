package com.example.jy_cake_it2.JY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Bid_shop_detail extends AppCompatActivity {

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
    private TextView letteringTextView;
    private int question_id;
    Button btn_bids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bid_shop_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //ㄴㅅidTextView = findViewById(R.id.idTextView);
        contentTextView = findViewById(R.id.contentTextView);
        createDateTextView = findViewById(R.id.createDateTextView);
        userTextView = findViewById(R.id.userTextView);
        typeTextView = findViewById(R.id.typeTextView);
        shapeTextView = findViewById(R.id.shapeTextView);
        colorTextView = findViewById(R.id.colorTextView);
        flavorTextView = findViewById(R.id.flavorTextView);
        pickupDateTextView = findViewById(R.id.pickupDateTextView);
        letteringTextView = findViewById(R.id.letteringTextView);

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
                    contentTextView.setText("요청사항 : " + orderDetail.getContent());
                    createDateTextView.setText("주문 생성 날짜 : " + orderDetail.getCreateDate());
                    userTextView.setText("주문자 이름 : " + orderDetail.getUser().getUsername());
                    typeTextView.setText("케이크 크기 : " + orderDetail.getCakeType());
                    shapeTextView.setText("케이크 모양 : " + orderDetail.getCakeShape());
                    colorTextView.setText("케이크 색 : " + orderDetail.getCakeColor());
                    flavorTextView.setText("맛 : " + orderDetail.getCakeFlavor());
                    pickupDateTextView.setText("픽업 날짜 : " + orderDetail.getPickupDate());
                    letteringTextView.setText("레터링 : " + orderDetail.getLettering());

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
                Toast.makeText( Bid_shop_detail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("AccessToken", null);
        btn_bids = findViewById(R.id.bidButton);
        btn_bids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText bidContent = findViewById(R.id.btn_content);
                EditText bidComment = findViewById(R.id.btn_comment);
                // EditText의 입력값을 문자열로 가져온 후 int로 변환
                String inputText = bidContent.getText().toString();

                // 만약 EditText에 값이 없으면 기본값을 0으로 설정
                int content = inputText.isEmpty() ? 0 : Integer.parseInt(inputText);
                String comment = bidComment.getText().toString();




                if (accessToken != null) {
                    Retrofit retrofit = RetrofitClient.getClient(accessToken);
                    LoginApiService apiService = retrofit.create(LoginApiService.class);
                    Bids bids = new Bids(content, comment);
                    Call<ApiResponse> call = apiService.createBid("Bearer " + accessToken, question_id, bids);

                    call.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            Toast.makeText(Bid_shop_detail.this, "code: " + response.code(), Toast.LENGTH_LONG).show();
                            if (response.isSuccessful()) {
                                    Toast.makeText(Bid_shop_detail.this, "성공! ", Toast.LENGTH_LONG).show();

                            }else{
                                Toast.makeText(Bid_shop_detail.this, "이미 입찰된 주문입니다. ", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            Toast.makeText(Bid_shop_detail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }
}