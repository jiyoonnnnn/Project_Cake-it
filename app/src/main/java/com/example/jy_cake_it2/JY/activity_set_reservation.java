package com.example.jy_cake_it2.JY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    private File cakeImageFile;;
    public ReservationData(ImageView cake_image, int year, int month, int day, int hour, int min) {
        this.cakeImageFile = cakeImageFile;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
    }
}

public class activity_set_reservation extends AppCompatActivity {
    File imageFile;
    Button btn_auction, btn_find_store;
    TimePicker set_time;
    DatePicker set_year;
    private int detailId;
    ImageView cake_image;
    //서버 전송을 위한 retrofit 초기화 및 인터페이스 연결
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://132.145.80.50:8888/")
            .addConverterFactory(GsonConverterFactory.create())//gson 으로 변환
            .build();
    // 인터페이스와 연결
    //RetrofitImgAPI retrofitImgAPI = retrofit.create(RetrofitImgAPI.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake_calendar);
        Intent intent = getIntent();

        // Intent로부터 이미지 파일 경로를 수신하고 복원
        String imagePath = intent.getStringExtra("imagePath");
        if (imagePath != null) {
            Log.d("Debug", "Received imagePath: " + imagePath); // 전달된 경로 한 번만 로그 출력

            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ImageView cake_imageView = findViewById(R.id.cake_image);
                cake_imageView.setImageBitmap(bitmap);

                // Bitmap을 파일로 변환하여 서버에 전송할 준비
                imageFile = imgFile;
            } else {
                Log.e("Error", "Image file does not exist at path: " + imagePath);
            }
        } else {
            Log.e("Error", "Image path is missing.");
            finish();
        }


        //케이크 이미지 (이전 페이지에서 설정한것) (DB에서 가져오기 or 이전 페이지에서 받아오기)
        cake_image = findViewById(R.id.cake_image);

        set_year = findViewById(R.id.set_year);
        set_time = findViewById(R.id.set_time);

        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);

        //날짜, accessToken, cake type등 초기화
        int year = set_year.getYear();
        int month = set_year.getMonth();
        int day = set_year.getDayOfMonth();
        int hour = set_time.getHour();
        int min = set_time.getMinute();
        String pickupDateStr = String.format("%04d-%02d-%02d %02d:%02d", year, month, day, hour, min);
        String accessToken = sharedPreferences.getString("AccessToken", null);
        String cakeTypeStr = intent.getStringExtra("cake_type") != null ? intent.getStringExtra("cake_type") : "";
        String cakeShapeStr = intent.getStringExtra("cake_shape") != null ? intent.getStringExtra("cake_shape") : "";
        String cakeColorStr = intent.getStringExtra("cake_color") != null ? intent.getStringExtra("cake_color") : "";
        String cakeFlavorStr = intent.getStringExtra("cake_flavor") != null ? intent.getStringExtra("cake_flavor") : "";
        String letteringStr = intent.getStringExtra("lettering") != null ? intent.getStringExtra("lettering") : "";


        //버튼 경매로 or 가게 둘러보기 페이지로 이동 + date, timepicker 에서 선택한 변수 케이크 도안이랑 같이 보내기
        btn_auction = findViewById(R.id.btn_auction);
        btn_auction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            //    EditText editsubject = findViewById(R.id.editID);
                EditText editcontent = findViewById(R.id.editPw);
                String ssubject = cakeTypeStr + "/" + cakeShapeStr + "/" + cakeFlavorStr;
                // RequestBody로 초기화
                RequestBody subject = RequestBody.create(MediaType.parse("text/plain"), ssubject);
                RequestBody content = RequestBody.create(MediaType.parse("text/plain"), editcontent.getText().toString());
                RequestBody cakeType = RequestBody.create(MediaType.parse("text/plain"), cakeTypeStr);
                RequestBody cakeShape = RequestBody.create(MediaType.parse("text/plain"), cakeShapeStr);
                RequestBody cakeColor = RequestBody.create(MediaType.parse("text/plain"), cakeColorStr);
                RequestBody cakeFlavor = RequestBody.create(MediaType.parse("text/plain"), cakeFlavorStr);
                RequestBody pickupDate = RequestBody.create(MediaType.parse("text/plain"), pickupDateStr);
                RequestBody lettering = RequestBody.create(MediaType.parse("text/plain"), letteringStr);
                RequestBody shop_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(0));
                // 로그를 통해 각 RequestBody에 담긴 데이터를 확인 (디버깅용)
               // Log.d("Debug", "Subject: " + editsubject.getText().toString());
                Log.d("Debug", "Content: " + editcontent.getText().toString());
                Log.d("Debug", "Cake Type: " + cakeTypeStr);
                Log.d("Debug", "Cake Shape: " + cakeShapeStr);
                Log.d("Debug", "Cake Color: " + cakeColorStr);
                Log.d("Debug", "Cake Flavor: " + cakeFlavorStr);
                Log.d("Debug", "Pickup Date: " + pickupDateStr);
                Log.d("Debug", "Lettering: " + letteringStr);

                // 이미지 파일이 null인지 확인
                if (imageFile == null || !imageFile.exists()) {
                    Toast.makeText(activity_set_reservation.this, "Image file is missing", Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);

                if (accessToken != null) {
                    Retrofit retrofit = RetrofitClient.getClient(accessToken);
                    LoginApiService apiService = retrofit.create(LoginApiService.class);

                    Call<Detail> call = apiService.createDetail("Bearer " + accessToken, subject, content, cakeType, cakeShape, cakeColor, cakeFlavor, pickupDate, lettering, shop_id, body);
                    call.enqueue(new Callback<Detail>() {
                        @Override
                        public void onResponse(Call<Detail> call, Response<Detail> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(activity_set_reservation.this, "Order created successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(activity_set_reservation.this, activity_draw_cake.class);
                                startActivity(intent);
                            } else {
                                try {
                                    String errorMessage = response.errorBody().string();
                                    Toast.makeText(activity_set_reservation.this, "Fail: " + response.code() + ", " + errorMessage, Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Detail> call, Throwable t) {
                            Toast.makeText(activity_set_reservation.this, "Network error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(activity_set_reservation.this, "No Access Token found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_find_store = findViewById(R.id.btn_find_store);
        btn_find_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

         //       EditText editsubject = findViewById(R.id.editID);
                EditText editcontent = findViewById(R.id.editPw);
                String ssubject = cakeTypeStr + "/" + cakeShapeStr + "/" + cakeFlavorStr;
                // RequestBody로 초기화
                RequestBody subject = RequestBody.create(MediaType.parse("text/plain"), ssubject);
                RequestBody content = RequestBody.create(MediaType.parse("text/plain"), editcontent.getText().toString());
                RequestBody cakeType = RequestBody.create(MediaType.parse("text/plain"), cakeTypeStr);
                RequestBody cakeShape = RequestBody.create(MediaType.parse("text/plain"), cakeShapeStr);
                RequestBody cakeColor = RequestBody.create(MediaType.parse("text/plain"), cakeColorStr);
                RequestBody cakeFlavor = RequestBody.create(MediaType.parse("text/plain"), cakeFlavorStr);
                RequestBody pickupDate = RequestBody.create(MediaType.parse("text/plain"), pickupDateStr);
                RequestBody lettering = RequestBody.create(MediaType.parse("text/plain"), letteringStr);
                RequestBody shop_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(0));
                // 로그를 통해 각 RequestBody에 담긴 데이터를 확인 (디버깅용)
          //      Log.d("Debug", "Subject: " + editsubject.getText().toString());
                Log.d("Debug", "Content: " + editcontent.getText().toString());
                Log.d("Debug", "Cake Type: " + cakeTypeStr);
                Log.d("Debug", "Cake Shape: " + cakeShapeStr);
                Log.d("Debug", "Cake Color: " + cakeColorStr);
                Log.d("Debug", "Cake Flavor: " + cakeFlavorStr);
                Log.d("Debug", "Pickup Date: " + pickupDateStr);
                Log.d("Debug", "Lettering: " + letteringStr);

                // 이미지 파일이 null인지 확인
                if (imageFile == null || !imageFile.exists()) {
                    Toast.makeText(activity_set_reservation.this, "Image file is missing", Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);

                if (accessToken != null) {
                    Retrofit retrofit = RetrofitClient.getClient(accessToken);
                    LoginApiService apiService = retrofit.create(LoginApiService.class);

                    Call<Detail> call = apiService.createDetail("Bearer " + accessToken, subject, content, cakeType, cakeShape, cakeColor, cakeFlavor, pickupDate, lettering, shop_id, body);
                    call.enqueue(new Callback<Detail>() {
                        @Override
                        public void onResponse(Call<Detail> call, Response<Detail> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(activity_set_reservation.this, "Order created successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(activity_set_reservation.this, Shop_click.class);
                                startActivity(intent);
                            } else {
                                try {
                                    String errorMessage = response.errorBody().string();
                                    Toast.makeText(activity_set_reservation.this, "Fail: " + response.code() + ", " + errorMessage, Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Detail> call, Throwable t) {
                            Toast.makeText(activity_set_reservation.this, "Network error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(activity_set_reservation.this, "No Access Token found", Toast.LENGTH_SHORT).show();
                }
            }


                // Retrofit을 통해 서버로 데이터 전송
//                if (year >= 2024 && 7 < hour && hour < 20){
//                    sendReservationDataToServer(cake_image, year, month, day, hour, min);
//                }

            public int getDetailId() {
                return detailId;
            }
        });

    }
    // Bitmap을 파일로 저장하는 메서드
    public File bitmapToFile(Bitmap bitmap, String fileName) {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, fileName + ".jpg");

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    private void uploadImageToServer(File file) {
        // 파일이 유효한지 확인

        // 파일을 MultipartBody.Part로 준비
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        // Retrofit 초기화

    }
    //전송을 어디서 합쳐야하는지 의문 업체 선정 or 입찰
//전송 버튼에 필요한 코드 -> 전송이 어떻게 이루어 지는지 정보 필요
//    String description = "Your description here"; // 필요한 설명 텍스트를 설정
//
//    // 날짜와 시간을 기반으로 파일 이름 설정
//    LocalDate now = LocalDate.now();
//    LocalTime now2 = LocalTime.now();
//    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH_mm_ss");
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//
//    String date_now = now.format(formatter);
//    String time_now = now2.format(formatter2);
//    String current_time = date_now + "_" + time_now;
//    String fileName = "cake_img_" + current_time;
//
//    // Bitmap을 파일로 저장
//    File imageFile = bitmapToFile(bitmap, fileName);
//
//    // 서버로 파일 전송
//        if (imageFile != null) {
//        uploadImageToServer(imageFile, description);
//    } else {
//        Log.e("Error", "Failed to save bitmap to file.");
//    }
//});



    // 현재 날짜와 시간을 기반으로 파일 이름을 생성하는 메서드
    private String generateCurrentTimestamp() {
        LocalDate now = LocalDate.now();
        LocalTime now2 = LocalTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH_mm_ss");
        return now.format(dateFormatter) + "_" + now2.format(timeFormatter);
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