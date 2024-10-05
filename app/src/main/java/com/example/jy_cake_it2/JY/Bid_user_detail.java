package com.example.jy_cake_it2.JY;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jy_cake_it2.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Bid_user_detail extends AppCompatActivity {
    private TextView idTextView;
    private TextView subjectTextView;
    private TextView contentTextView;
    private TextView createDateTextView;
    private RecyclerView recyclerView;
    private BidsAdapter bidsAdapter;
    private List<Bids> bidsList = new ArrayList<>();
    private TextView userTextView;
    private TextView modifyDateTextView;
    private TextView typeTextView;
    private TextView shapeTextView;
    private TextView colorTextView;
    private TextView flavorTextView;
    private TextView pickupDateTextView;
    private TextView letteringTextView;
    private int question_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bid_user_detail);

        idTextView = findViewById(R.id.idTextView);
        subjectTextView = findViewById(R.id.subjectTextView);
        contentTextView = findViewById(R.id.contentTextView);
        createDateTextView = findViewById(R.id.createDateTextView);

        recyclerView = findViewById(R.id.answerRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bidsAdapter = new BidsAdapter(new ArrayList<>());
        recyclerView.setAdapter(bidsAdapter);

        userTextView = findViewById(R.id.userTextView);
        modifyDateTextView = findViewById(R.id.modifyDateTextView);
        typeTextView = findViewById(R.id.typeTextView);
        shapeTextView = findViewById(R.id.shapeTextView);
        colorTextView = findViewById(R.id.colorTextView);
        flavorTextView = findViewById(R.id.flavorTextView);
        pickupDateTextView = findViewById(R.id.pickupDateTextView);
        letteringTextView = findViewById(R.id.letteringTextView);

//        BidsAdapter adapter = new BidsAdapter(bidsList);
//        bidsAdapter = new BidsAdapter(new ArrayList<>(), new BidsAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Bids bids) {
//                Toast.makeText(Bid_user_detail.this, "수정중 ", Toast.LENGTH_LONG).show();
                // 클릭 시 주문 세부사항으로 이동
//                Intent intent = new Intent(Bid_user_detail.this, Bid_user_detail.class);
//                intent.putExtra("ORDER_ID", detail.getId());
//                startActivity(intent);
//            }
//        });
//        recyclerView.setAdapter(bidsAdapter);

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
                    subjectTextView.setText("subject : " + orderDetail.getSubject());
                    contentTextView.setText("content : " + orderDetail.getContent());
                    createDateTextView.setText("create_date : " + orderDetail.getCreateDate());

                    List<Bids> bids = orderDetail.getAnswers();
                    bidsAdapter.updateBids(bids);
//                    bidsAdapter = new BidsAdapter(bids);
//                    recyclerView.setAdapter(bidsAdapter);
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
                Toast.makeText( Bid_user_detail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        TextView btn1;
        btn1 = findViewById(R.id.back);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bid_user_detail.this, activity_browse.class);
                startActivity(intent);
            }
        });

    }
}