package com.example.jy_cake_it2.JY;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Bid_user_detail extends AppCompatActivity {
    private TextView idTextView, subjectTextView, contentTextView, createDateTextView;
    private RecyclerView recyclerView;
    private BidsAdapter bidsAdapter;
    private List<Bids> bidsList = new ArrayList<>();
    private TextView userTextView, modifyDateTextView, typeTextView, shapeTextView, colorTextView, flavorTextView, pickupDateTextView, letteringTextView, statusTextView,shopTextView;
    private int question_id;
    ImageView cakeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bid_user_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView btn1 = findViewById(R.id.back);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bid_user_detail.this, activity_browse.class);
                startActivity(intent);
            }
        });
        // TextView 초기화
        cakeImage = findViewById(R.id.cake_image);
        idTextView = findViewById(R.id.idTextView);
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
        statusTextView = findViewById(R.id.statusTextView);
        shopTextView = findViewById(R.id.shopTextView);

        // RecyclerView 초기화 및 어댑터 설정
        recyclerView = findViewById(R.id.answerRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 초기 어댑터 설정 (빈 리스트로 시작)
        bidsAdapter = new BidsAdapter(bidsList, new BidsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Bids bid) {
                showBidDialog(question_id, bid);
            }
        });
        recyclerView.setAdapter(bidsAdapter);

        // Intent로부터 question_id 받기
        Intent intent = getIntent();
        question_id = intent.getIntExtra("ORDER_ID", -1);

        // 주문 세부사항 가져오기
        fetchOrderDetail(question_id);
    }

    private void showBidDialog(int question_id, Bids bid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_custom, null);
        builder.setView(dialogView);

        TextView dialogShopName = dialogView.findViewById(R.id.dialog_shop_name);
        Button confirmButton = dialogView.findViewById(R.id.dialog_confirm_button);
        Button cancelButton = dialogView.findViewById(R.id.dialog_cancel_button);

        dialogShopName.setText("Shop Name: " + bid.getShop().getShopname());
        AlertDialog dialog = builder.create();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleConfirmBid(question_id, bid);
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void handleConfirmBid(int question_id, Bids bid) {
        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("AccessToken", null);
        int shop_id = bid.getShop().getId();
        if (accessToken != null) {
            Retrofit retrofit = RetrofitClient.getClient(accessToken);
            LoginApiService apiService = retrofit.create(LoginApiService.class);
            SelectShop request = new SelectShop(question_id, shop_id);
            Call<SelectShop> call = apiService.selectShop("Bearer " + accessToken, request);

            call.enqueue(new Callback<SelectShop>() {
                @Override
                public void onResponse(Call<SelectShop> call, Response<SelectShop> response) {
                    if (response.isSuccessful() ) {
                        Toast.makeText(Bid_user_detail.this, "Confirmed bid from " + bid.getShop().getShopname(), Toast.LENGTH_SHORT).show();
//                        handleChangeStatus(question_id);
                    }else {
                        Toast.makeText(Bid_user_detail.this, "실패" + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<SelectShop> call, Throwable t) {
                    Toast.makeText(Bid_user_detail.this, "실패", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
//    private void handleChangeStatus(int question_id) {
//        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
//        String accessToken = sharedPreferences.getString("AccessToken", null);
//        if (accessToken != null) {
//            Retrofit retrofit = RetrofitClient.getClient(accessToken);
//            LoginApiService apiService = retrofit.create(LoginApiService.class);
//            ChangeStatus request = new ChangeStatus(question_id, 30);
//            Call<ChangeStatus> call = apiService.changeStatus("Bearer " + accessToken, request);
//
//            call.enqueue(new Callback<ChangeStatus>() {
//                @Override
//                public void onResponse(Call<ChangeStatus> call, Response<ChangeStatus> response) {
//                    if (response.isSuccessful() ) {
//                        Toast.makeText(Bid_user_detail.this, "주문상태 변경", Toast.LENGTH_SHORT).show();
//                    }else {
//                        Toast.makeText(Bid_user_detail.this, "실패" + response.code(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//                @Override
//                public void onFailure(Call<ChangeStatus> call, Throwable t) {
//                    Toast.makeText(Bid_user_detail.this, "실패", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
    private void fetchOrderDetail(int question_id) {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://132.145.80.50:9999/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        LoginApiService apiService = retrofit.create(LoginApiService.class);

        Call<Detail> call = apiService.getDetail(question_id);
        call.enqueue(new Callback<Detail>() {
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Detail orderDetail = response.body();

                    // 세부 사항 업데이트
                    subjectTextView.setText("subject : " + orderDetail.getSubject());
                    contentTextView.setText("content : " + orderDetail.getContent());
                    createDateTextView.setText("create_date : " + orderDetail.getCreateDate());

                    // 입찰 리스트 업데이트
                    List<Bids> bids = orderDetail.getAnswers();
                    bidsAdapter.updateBids(bids); // 어댑터에 새로운 데이터 제공

                    userTextView.setText("username : " + orderDetail.getUser().getUsername());
                    modifyDateTextView.setText("modify_date : " + orderDetail.getModifyDate());
                    typeTextView.setText("cake_type : " + orderDetail.getCakeType());
                    shapeTextView.setText("cake_shape : " + orderDetail.getCakeShape());
                    colorTextView.setText("cake_color : " + orderDetail.getCakeColor());
                    flavorTextView.setText("cake_flavor : " + orderDetail.getCakeFlavor());
                    pickupDateTextView.setText("pickup_date : " + orderDetail.getPickupDate());
                    letteringTextView.setText("lettering : " + orderDetail.getLettering());
                    statusTextView.setText("상태 : " + orderDetail.getOrderStatus());
                    shopTextView.setText("가게 : " + orderDetail.getShopId());
                    fetchCakeImage(orderDetail.getCakeIMG());

//                    cakeImage.setImageBitmap(response.body());
//                    cakeImage.getDrawable();

                } else if (response.code() == 307) {
                    String newLocation = response.raw().header("Location");
                    if (newLocation != null) {
                        subjectTextView.setText("Temporary redirect to: " + newLocation);
                    } else {
                        subjectTextView.setText("Temporary redirect, but no new location provided");
                    }
                }
            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                Toast.makeText(Bid_user_detail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    cakeImage.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(Bid_user_detail.this, "이미지를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Bid_user_detail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}