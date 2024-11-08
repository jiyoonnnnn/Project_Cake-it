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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
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

public class user_order_detail extends AppCompatActivity {
    private TextView idTextView, subjectTextView, contentTextView, createDateTextView;
    private RecyclerView recyclerView;
    private BidsAdapter bidsAdapter;
    private List<Bids> bidsList = new ArrayList<>();
    private TextView userTextView, modifyDateTextView, typeTextView, shapeTextView, colorTextView, flavorTextView, pickupDateTextView, letteringTextView, statusTextView,shopTextView;
    private int question_id;
    private Button paymentButton;
    ImageView cakeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_order_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView btn1 = findViewById(R.id.paymentButton);
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(user_order_detail.this, activity_browse.class);
//                startActivity(intent);
//            }
//        });
        // TextView 초기화
        cakeImage = findViewById(R.id.cake_image);
        contentTextView = findViewById(R.id.contentTextView);
        createDateTextView = findViewById(R.id.createDateTextView);
        typeTextView = findViewById(R.id.typeTextView);
        shapeTextView = findViewById(R.id.shapeTextView);
        colorTextView = findViewById(R.id.colorTextView);
        flavorTextView = findViewById(R.id.flavorTextView);
        pickupDateTextView = findViewById(R.id.pickupDateTextView);
        letteringTextView = findViewById(R.id.letteringTextView);
        statusTextView = findViewById(R.id.statusTextView);
        shopTextView = findViewById(R.id.shopTextView);
        paymentButton=findViewById(R.id.paymentButton);;

        // RecyclerView 초기화 및 어댑터 설정
        recyclerView = findViewById(R.id.answerRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
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
        TextView dialogPrice = dialogView.findViewById(R.id.dialog_price);
        TextView dialogComment = dialogView.findViewById(R.id.dialog_comment);
        Button confirmButton = dialogView.findViewById(R.id.dialog_confirm_button);
        TextView cancelButton = dialogView.findViewById(R.id.dialog_cancel_button);

        dialogShopName.setText("가게명: " + bid.getShop().getShopname());
        dialogPrice.setText("입찰 금액: " + bid.getContent());
        dialogComment.setText("입찰 한 마디: " + bid.getComment());
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

//        paymentButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(user_order_detail.this, "대상 가게의 페이지에서 계좌번호를 확인하고 입금하세요. " + bid.getShop().getShopname(), Toast.LENGTH_SHORT).show();
//            }
//        });

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
                        Toast.makeText(user_order_detail.this, "Confirmed bid from " + bid.getShop().getShopname(), Toast.LENGTH_SHORT).show();
//                        handleChangeStatus(question_id);
                    }else {
                        Toast.makeText(user_order_detail.this, "실패" + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<SelectShop> call, Throwable t) {
                    Toast.makeText(user_order_detail.this, "실패", Toast.LENGTH_SHORT).show();
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
//                public void onFailure(Call<ChangeㅂㅈStatus> call, Throwable t) {
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
                    if (orderDetail.getOrderStatus() == 20) {
                        recyclerView.setVisibility(View.VISIBLE); // 주문 코드가 20번일 경우 보이게 설정
                    } else {
                        recyclerView.setVisibility(View.GONE); // 그 외의 경우 숨기기
                    }
                    if (orderDetail.getOrderStatus() == 33) {
                        paymentButton.setVisibility(View.VISIBLE); // 주문 코드가 20번일 경우 보이게 설정
                    } else {
                        paymentButton.setVisibility(View.GONE); // 그 외의 경우 숨기기
                    }
                    // 세부 사항 업데이트
                    contentTextView.setText("content : " + orderDetail.getContent());
                    createDateTextView.setText("create_date : " + orderDetail.getCreateDate());

                    // 입찰 리스트 업데이트
                    List<Bids> bids = orderDetail.getAnswers();
                    bidsAdapter.updateBids(bids); // 어댑터에 새로운 데이터 제공
                    int itemCount = bids.size();
                    int itemHeight = bidsAdapter.getItemHeight(recyclerView); // 각 항목의 높이
                    int totalHeight = itemHeight * itemCount;

                    ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
                    params.height = totalHeight;
                    recyclerView.setLayoutParams(params);

//                    userTextView.setText("주문자 이름 : " + orderDetail.getUser().getUsername());
                    typeTextView.setText("케이크 크기 : " + orderDetail.getCakeType());
                    shapeTextView.setText("케이크 모양 : " + orderDetail.getCakeShape());
                    colorTextView.setText("케이크 색 : " + orderDetail.getCakeColor());
                    flavorTextView.setText("맛 : " + orderDetail.getCakeFlavor());
                    pickupDateTextView.setText("픽업 날짜 : " + orderDetail.getPickupDate());
                    letteringTextView.setText("레터링 : " + orderDetail.getLettering());

                    fetchshopId(orderDetail.getShopId());
                    fetchCakeImage(orderDetail.getCakeIMG());

//                    cakeImage.setImageBitmap(response.body());
//                    cakeImage.getDrawable();

                }
            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                Toast.makeText(user_order_detail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private  void fetchshopId(int ShopId) {
        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("AccessToken", null);
        if (accessToken != null) {
            Retrofit retrofit = RetrofitClient.getClient(accessToken);
            LoginApiService apiService = retrofit.create(LoginApiService.class);
            Call<StoreInfoResponse> call = apiService.getStorename("Bearer " + accessToken, ShopId);

            call.enqueue(new Callback<StoreInfoResponse>() {
                @Override
                public void onResponse(Call<StoreInfoResponse> call, Response<StoreInfoResponse> response) {
                    Toast.makeText(user_order_detail.this, "code: " + response.code(), Toast.LENGTH_LONG).show();
                    if (response.isSuccessful() && response.body() != null) {
                        StoreInfoResponse userInfo = response.body();
                        if (ShopId == 0){
                            shopTextView.setVisibility(View.GONE);
                        }else{
                            shopTextView.setText("가게 : " + userInfo.getShopname());
                        }

                    } else {
                        Toast.makeText(user_order_detail.this, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<StoreInfoResponse> call, Throwable t) {
                    Toast.makeText(user_order_detail.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
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
                    Toast.makeText(user_order_detail.this, "이미지를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(user_order_detail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}