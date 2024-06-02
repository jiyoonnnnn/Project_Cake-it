package com.example.jy_cake_it2.JY;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class Detail_Post_Exemple extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_post_exemple);

        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("AccessToken", null);

        if (accessToken != null) {
            Retrofit retrofit = RetrofitClient.getClient(accessToken);
            LoginApiService apiService = retrofit.create(LoginApiService.class);

            Detail requestBody = new Detail(
                    "Cake Order", "Please make a chocolate cake.", "Chocolate", "Round", "Blue", "Vanilla", "2024-05-23", "Happy Birthday", 1
            );
            Call<Detail> call = apiService.createDetail("Bearer " + accessToken, requestBody);

            call.enqueue(new Callback<Detail>() {
                @Override
                public void onResponse(Call<Detail> call, Response<Detail> response) {
                    if (response.isSuccessful()) {
                        // 응답 성공 처리 로직
                        Detail detailResponse = response.body();
                        if (detailResponse != null) {
                            // 성공 시의 처리 로직
                            Toast.makeText(Detail_Post_Exemple.this, "Order created successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Detail_Post_Exemple.this, "Response body is null", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // 응답 실패 처리 로직
                        Toast.makeText(Detail_Post_Exemple.this, "Failed to create order", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Detail> call, Throwable t) {
                    // 네트워크 실패 처리 로직
                    Toast.makeText(Detail_Post_Exemple.this, "Network error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No Access Token found", Toast.LENGTH_SHORT).show();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}