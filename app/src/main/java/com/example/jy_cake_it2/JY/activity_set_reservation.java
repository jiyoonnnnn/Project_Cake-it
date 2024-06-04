package com.example.jy_cake_it2.JY;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
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
    ImageView cake_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake_calendar);

        //케이크 이미지 (이전 페이지에서 설정한것) (DB에서 가져오기 or 이전 페이지에서 받아오기)
        cake_image = findViewById(R.id.cake_image);
        cake_image.setImageResource(R.drawable.cake_test);

        set_year = findViewById(R.id.set_year);
        set_time = findViewById(R.id.set_time);

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