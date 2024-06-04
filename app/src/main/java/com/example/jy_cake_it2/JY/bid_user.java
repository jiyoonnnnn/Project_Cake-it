package com.example.jy_cake_it2.JY;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class bid_user extends AppCompatActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bid_user);

        subjectTextView = findViewById(R.id.subjectTextView);
        contentTextView = findViewById(R.id.contentTextView);
        createDateTextView = findViewById(R.id.createDateTextView);
        userTextView = findViewById(R.id.userTextView);
        modifyDateTextView = findViewById(R.id.modifyDateTextView);
        typeTextView = findViewById(R.id.typeTextView);
        shapeTextView = findViewById(R.id.shapeTextView);
        colorTextView = findViewById(R.id.colorTextView);
        flavorTextView = findViewById(R.id.flavorTextView);
        pickupDateTextView = findViewById(R.id.pickupDateTextView);
        letteringTextView = findViewById(R.id.letteringTextView);

        int question_id = 17;



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
                    subjectTextView.setText("subject : " + orderDetail.getSubject());
                    contentTextView.setText("content : " + orderDetail.getContent());
                    createDateTextView.setText("create_date : " + orderDetail.getCreateDate());
                    userTextView.setText("username : " + orderDetail.getUser().getUsername());
                    modifyDateTextView.setText("modify_date : " + orderDetail.getModifyDate());
                    typeTextView.setText("cake_type : " + orderDetail.getCakeType());
                    shapeTextView.setText("cake_shape : " + orderDetail.getCakeShape());
                    colorTextView.setText("cake_color : " + orderDetail.getCakeColor());
                    flavorTextView.setText("cake_flavor : " + orderDetail.getCakeFlavor());
                    pickupDateTextView.setText("pickup_date : " + orderDetail.getPickupDate());
                    letteringTextView.setText("lettering : " + orderDetail.getLettering());

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
                Toast.makeText( bid_user.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}