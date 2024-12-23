package com.example.jy_cake_it2.JY;

import android.content.Intent;
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

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_shopsignup extends AppCompatActivity {

    TextView back;
    EditText editName,editPhone,editPw,editPw2,editEmail,editShopname,editAddress,editLocX,editLocY, editBank;
    Button pwcheck, btn2, convertAddressButton;
    private TextView dataTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopsignup);

        //기입 항목

        dataTextView = findViewById(R.id.signNametext);
        editName = findViewById(R.id.signName);
        editPw=findViewById(R.id.signPW);
        editPw2=findViewById(R.id.signPW2);
        editShopname=findViewById(R.id.signShopname);
        editAddress=findViewById(R.id.signAdress);
        editPhone=findViewById(R.id.signShopPhone);
        editEmail=findViewById(R.id.signmail);
        editLocX = findViewById(R.id.signLocX);
        editLocY = findViewById(R.id.signLocY);
        editBank = findViewById(R.id.signBank);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://132.145.80.50:9999/") // 기본 URL만 입력
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginApiService service = retrofit.create(LoginApiService.class);

        //        비밀번호 확인 버튼
        pwcheck = findViewById(R.id.pwcheckbutton);
        pwcheck.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String pw = editPw.getText().toString();
                String pw2= editPw2.getText().toString();
                if (!pw.equals(pw2)) {
                    Toast.makeText(activity_shopsignup.this, "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
                } else {
                    pwcheck.setText("일치");
                }
            }
        });

        btn2 = findViewById(R.id.signupbutton);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_shopsignup.this, LoginShop.class);
                startActivity(intent);

                String name = editName.getText().toString();
                String pw = editPw.getText().toString();
                String pw2= editPw2.getText().toString();
                String sname= editShopname.getText().toString();
                String add= editAddress.getText().toString();
                String phone = editPhone.getText().toString();
                String email = editEmail.getText().toString();
                String bank = editBank.getText().toString();
                // 이메일 가져온다. 이메일 형식체크
                String loc_x = editLocX.getText().toString();
                String loc_y = editLocY.getText().toString();
                Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;
                if(pattern.matcher(email).matches() == false){
                    Toast.makeText(activity_shopsignup.this
                            , "이메일 형식이 올바르지 않습니다."
                            , Toast.LENGTH_SHORT).show();
                    return;
                }
                ShopAccount shop = new ShopAccount(name,  email, pw, pw2, sname, phone, add, loc_x, loc_y, bank);

                Call<ApiResponse> postCall = service.createShopAccount(shop);
                postCall.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        Toast.makeText(activity_shopsignup.this, "code: " + response.code() , Toast.LENGTH_LONG).show();
                        // 응답 처리
                        if (response.isSuccessful()){

                            ApiResponse apiResponse = response.body();

                        }else if (response.code() == 307) {
                            // 임시 리디렉션 응답을 받은 경우, 새로운 위치로 재시도
                            String newLocation = response.raw().header("Location");
                            if (newLocation != null) {
                                // 새로운 위치로 재시도
                                // newLocation에 있는 URL로 다시 요청을 보내야 합니다.
                                dataTextView.setText("Temporary redirect to: " + newLocation);
                            } else {
                                // 새로운 위치가 제공되지 않은 경우에 대한 처리
                                dataTextView.setText("Temporary redirect, but no new location provided");
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse> postCall, Throwable t) {
                        t.printStackTrace();
                        // 요청이 실패한 경우 처리하는 코드 작성

                    }
                });
            }
        });
        TextView btn1;
        btn1 = findViewById(R.id.back);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_shopsignup.this, LoginShop.class);
                startActivity(intent);
            }
        });
        convertAddressButton = findViewById(R.id.convertAddressButton);
        convertAddressButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_shopsignup.this, Geocoding.class);
                startActivityForResult(intent, 1); // Request code 1로 Geocoding 액티비티 호출
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String latitude = data.getStringExtra("latitude");
            String longitude = data.getStringExtra("longitude");

            if (latitude != null && longitude != null) {
                editLocX.setText(latitude);
                editLocY.setText(longitude);
            }
        }
    }
}