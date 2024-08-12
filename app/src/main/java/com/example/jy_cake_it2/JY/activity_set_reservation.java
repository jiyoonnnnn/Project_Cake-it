package com.example.jy_cake_it2.JY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jy_cake_it2.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;


interface ReservationApiService{
    
    //post할 url 경로 따로 설정해줘야 함
    @POST("api/shop/list")
    Call<Void> sendReservationData(@Body ReservationData reservationData);
}
class ReservationData {
    //image view로는 전송 못하니 다른방법 생각 + res파일에 있는 이미지 말고 따로 이미지 이전 페이지에서 전달 받아와야함
    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;
    private ImageView cake_image;
    public ReservationData(ImageView cake_image, int year, int month, int day, int hour, int min) {
        this.cake_image = cake_image;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
    }
}

public class activity_set_reservation extends AppCompatActivity {

    Button btn_auction, btn_find_store;
    TimePicker set_time;
    DatePicker set_year;
    private int detailId;
    ImageView cake_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake_calendar);

        TextView btn1;
        btn1 = findViewById(R.id.back);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_set_reservation.this, activity_draw_cake.class);
                startActivity(intent);
            }
        });


        //케이크 이미지 (이전 페이지에서 설정한것) (DB에서 가져오기 or 이전 페이지에서 받아오기)
        cake_image = findViewById(R.id.cake_image);
  //      cake_image.setImageResource(R.drawable.cake_test);

        set_year = findViewById(R.id.set_year);
        set_time = findViewById(R.id.set_time);

        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("AccessToken", null);

        //버튼 경매로 or 가게 둘러보기 페이지로 이동 + date, timepicker 에서 선택한 변수 케이크 도안이랑 같이 보내기
        btn_auction = findViewById(R.id.btn_auction);
        btn_auction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = set_year.getYear();
                int month = set_year.getMonth();
                int day = set_year.getDayOfMonth();
                int hour = set_time.getHour();
                int min = set_time.getMinute();

                // Retrofit을 통해 서버로 데이터 전송
                if (year >= 2024 && 7 < hour && hour < 20){
                    sendReservationDataToServer(cake_image, year, month, day, hour, min);
                }
            }
        });
        btn_find_store = findViewById(R.id.btn_find_store);
        btn_find_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = set_year.getYear();
                int month = set_year.getMonth() + 1;
                int day = set_year.getDayOfMonth();
                int hour = set_time.getHour();
                int min = set_time.getMinute();

                String pickup_date = String.format("%04d-%02d-%02d %02d:%02d", year, month, day, hour, min);
                EditText editsubject = findViewById(R.id.editID);
                EditText editcontent = findViewById(R.id.editPw);
                Intent intent = getIntent();
                String subject = editsubject.getText().toString();
                String content = editcontent.getText().toString();
                String cake_type = intent.getStringExtra("cake_type");
                String cake_shape = intent.getStringExtra("cake_shape");
                String cake_color = intent.getStringExtra("cake_color");
                String cake_flavor = intent.getStringExtra("cake_flavor");
                String lettering = intent.getStringExtra("lettering");
                int shop_id = 6;

                if (accessToken != null) {
                    Retrofit retrofit = RetrofitClient.getClient(accessToken);
                    LoginApiService apiService = retrofit.create(LoginApiService.class);

                    Detail detail = new Detail(subject, content, cake_type, cake_shape, cake_color, cake_flavor, pickup_date, lettering, shop_id);

                    Call<Detail> call = apiService.createDetail("Bearer " + accessToken, detail);
                    call.enqueue(new Callback<Detail>() {
                        @Override
                        public void onResponse(Call<Detail> call, Response<Detail> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공 처리 로직
//                        Detail detailResponse = response.body();
//                        if (detailResponse != null) {
                                // 성공 시의 처리 로직
                                Detail detail = response.body();
//                                int detailId = detail.getId();
                                Toast.makeText(activity_set_reservation.this, "Order created successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(activity_set_reservation.this, activity_browse.class);
//                                intent.putExtra("DETAIL_ID", detailId);
                                startActivity(intent);
//                        } else {
//                            Toast.makeText(Detail_Post_Exemple.this, "Response body is null"+response.code(), Toast.LENGTH_SHORT).show();
//                        }
                            } else {
                                // 응답 실패 처리 로직
                                Toast.makeText(activity_set_reservation.this, "Fail:"+response.code(), Toast.LENGTH_SHORT).show();
//                                try {
//                                    // 서버가 반환한 오류 메시지 확인
////                                    String errorBody = response.errorBody().string();
//                                    Toast.makeText(activity_draw_cake.this, "Error message: ", Toast.LENGTH_LONG).show();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Detail> call, Throwable t) {
                            // 네트워크 실패 처리 로직
                            Toast.makeText(activity_set_reservation.this, "Network error", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(activity_set_reservation.this, "No Access Token found", Toast.LENGTH_SHORT).show();
                }


                // Retrofit을 통해 서버로 데이터 전송
//                if (year >= 2024 && 7 < hour && hour < 20){
//                    sendReservationDataToServer(cake_image, year, month, day, hour, min);
//                }
            }
            public int getDetailId() {
                return detailId;
            }
        });

    }
    //retrofit 을 이용 서버로 데이터 전송
    //이미지 부분은 DB에 있는걸 사용할거기 때문에 아직은 추가 X 테스트 용으로 String으로 파일 경로 전달 가능
    public void sendReservationDataToServer(ImageView cake_image, int year, int month, int day, int hour, int min){
        ReservationData reservationData = new ReservationData(cake_image, year, month, day, hour, min);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://132.145.80.50:9999/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ReservationApiService reservationApiService = retrofit.create(ReservationApiService.class);
        Call<Void> call = reservationApiService.sendReservationData(reservationData);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 성공 처리
                    //새로운 경매/가게 액티비티로 넘어가게 하기
                } else {
                    // 실패 처리
                    Toast.makeText(activity_set_reservation.this, "데이터 전송 및 예약에 실패했습니다, 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // 오류 처리
                Toast.makeText(activity_set_reservation.this, "데이터 전송 중 오류가 발생하였습니다, 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}