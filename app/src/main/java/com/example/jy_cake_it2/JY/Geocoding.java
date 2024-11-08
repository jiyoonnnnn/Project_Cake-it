package com.example.jy_cake_it2.JY;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jy_cake_it2.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Geocoding extends AppCompatActivity {

    private static final String CLIENT_ID = "3x509ra2h1"; // 네이버 클라이언트 ID
    private static final String CLIENT_SECRET = "qKnlWljHsXOqPsSI6S9oTLh4PUa0aiIfpZKwi0vL"; // 네이버 클라이언트 Secret
    private static final String BASE_URL = "https://naveropenapi.apigw.ntruss.com/";

    private EditText addressInput;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocoding);

        addressInput = findViewById(R.id.address_input);
        resultText = findViewById(R.id.result_text);
        Button convertButton = findViewById(R.id.convert_button);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NaverGeocodingService service = retrofit.create(NaverGeocodingService.class);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressInput.getText().toString();
                if (address.isEmpty()) {
                    resultText.setText("주소를 입력해주세요.");
                    return;
                }

                service.getGeocoding(CLIENT_ID, CLIENT_SECRET, address).enqueue(new Callback<GeocodingResponse>() {
                    @Override
                    public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            GeocodingResponse result = response.body();
                            if (!result.addresses.isEmpty()) {
                                GeocodingResponse.Address address = result.addresses.get(0);
                                String lat = address.y;
                                String lng = address.x;
                                String resultString = "위도: " + lat + ", 경도: " + lng;
                                resultText.setText(resultString);
                                Log.d("GeoTest", resultString);

                                // 결과 값을 반환하여 activity_shopsignup에서 사용할 수 있게 한다
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("latitude", lat);
                                resultIntent.putExtra("longitude", lng);
                                setResult(RESULT_OK, resultIntent);
                                finish(); // Geocoding 액티비티 종료
                            } else {
                                resultText.setText("주소를 찾을 수 없습니다.");
                            }
                        } else {
                            resultText.setText("오류 발생: " + response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                        resultText.setText("네트워크 오류: " + t.getMessage());
                    }
                });
            }
        });
    }
}