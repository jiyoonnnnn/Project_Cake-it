package com.example.jy_cake_it2.JY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jy_cake_it2.R;

public class activity_draw_cake extends AppCompatActivity {

    TextView mainText1, mainText2;
    Button btnOrder, btnGetPicture, btn_ai;
    Spinner spnColor, spnShape, spnTopping, spnLettering, spnLetterShape;
    ImageView topping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_cake);

        TextView btn1;
        btn1 = findViewById(R.id.back);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_draw_cake.this, Login_main.class);
                startActivity(intent);
            }
        });

        // TextView 초기화
        mainText1 = findViewById(R.id.main_text1);
        mainText2 = findViewById(R.id.main_text2);


        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("AccessToken", null);
        // Button 초기화
        btnOrder = findViewById(R.id.btn_order);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 주문하기 버튼 클릭 이벤트 처리
                // 이미지, 각 스피너에 있는 데이터들 모아놨다 마지막에 전송 + 케이크 이미지 가져오기
                String cake_type = "1호";
                String cake_shape = spnShape.getSelectedItem().toString();
                String cake_color = spnColor.getSelectedItem().toString();
                String cake_flavor = "초코시트";
                String lettering = spnLettering.getSelectedItem().toString();
                Intent intent = new Intent(activity_draw_cake.this, activity_set_reservation.class);
                intent.putExtra("cake_type", cake_type);
                intent.putExtra("cake_shape", cake_shape);
                intent.putExtra("cake_color", cake_color);
                intent.putExtra("cake_flavor", cake_flavor);
                intent.putExtra("lettering", lettering);

                // 다음 페이지로 이동
                startActivity(intent);
            }
        });

        btnGetPicture = findViewById(R.id.btn_get_picture);
        btnGetPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사진 불러오기 버튼 클릭 이벤트 처리
            }
        });

        btn_ai = findViewById(R.id.btn_ai);
        btn_ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_draw_cake.this,activity_browse.class);
                startActivity(intent);
            }
        });

        // Spinner 초기화
        spnColor = findViewById(R.id.spn_color);
        String[] cake_colors = getResources().getStringArray(R.array.cake_color);
        ArrayAdapter<String> cakeColorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cake_colors);
        spnColor.setAdapter(cakeColorAdapter);
        spnColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int color, long l) {
                switch (color) {
                    case 0:
                        findViewById(R.id.circle).setBackgroundColor(getResources().getColor(R.color.white, getTheme()));
                        findViewById(R.id.rectangle).setBackgroundColor(getResources().getColor(R.color.white, getTheme()));
                        break;
                    case 1:
                        findViewById(R.id.circle).setBackgroundColor(getResources().getColor(R.color.red, getTheme()));
                        findViewById(R.id.rectangle).setBackgroundColor(getResources().getColor(R.color.red, getTheme()));
                        break;
                    case 2:
                        findViewById(R.id.circle).setBackgroundColor(getResources().getColor(R.color.light_pink, getTheme()));
                        findViewById(R.id.rectangle).setBackgroundColor(getResources().getColor(R.color.light_pink, getTheme()));
                        break;
                    case 3:
                        findViewById(R.id.circle).setBackgroundColor(getResources().getColor(R.color.light_blue_A200, getTheme()));
                        findViewById(R.id.rectangle).setBackgroundColor(getResources().getColor(R.color.light_blue_A200, getTheme()));
                        break;
                    case 4:
                        findViewById(R.id.circle).setBackgroundColor(getResources().getColor(R.color.yellow, getTheme()));
                        findViewById(R.id.rectangle).setBackgroundColor(getResources().getColor(R.color.yellow, getTheme()));
                        break;
                    case 5:
                        findViewById(R.id.circle).setBackgroundColor(getResources().getColor(R.color.black, getTheme()));
                        findViewById(R.id.rectangle).setBackgroundColor(getResources().getColor(R.color.black, getTheme()));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // 아무것도 선택되지 않았을 때 처리할 내용
                findViewById(R.id.circle).setBackgroundColor(getResources().getColor(R.color.white, null));
                findViewById(R.id.rectangle).setBackgroundColor(getResources().getColor(R.color.white, null));
            }
        });

        spnShape = findViewById(R.id.spn_shape);
        String[] cake_shapes = getResources().getStringArray(R.array.cake_shape);
        ArrayAdapter<String> cakeShapeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cake_shapes);
        spnShape.setAdapter(cakeShapeAdapter);
        spnShape.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        findViewById(R.id.circle).setVisibility(View.VISIBLE);
                        findViewById(R.id.rectangle).setVisibility(View.GONE);
                        break;
                    case 1:
                        findViewById(R.id.circle).setVisibility(View.GONE);
                        findViewById(R.id.rectangle).setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                findViewById(R.id.circle).setVisibility(View.VISIBLE);
                findViewById(R.id.rectangle).setVisibility(View.GONE);
            }
        });

        topping = findViewById(R.id.topping);
        spnTopping = findViewById(R.id.spn_topping);
        String[] cake_toppings = getResources().getStringArray(R.array.cake_topping);
        ArrayAdapter<String> cakeToppingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cake_toppings);
        spnTopping.setAdapter(cakeToppingAdapter);
        spnTopping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // 토핑 선택에 따른 처리
                switch (position) {
                    case 0:
                        // 토핑 1 처리
                        break;
                    case 1:
                        // 토핑 2 처리
                        break;
                    case 2:
                        // 토핑 3 처리
                        break;
                    case 3:
                        // 토핑 4 처리
                        break;
                    case 4:
                        // 토핑 5 처리
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // 아무것도 선택되지 않았을 때 처리할 내용
            }
        });

        spnLettering = findViewById(R.id.spn_lettering);
        String[] cake_lettering = getResources().getStringArray(R.array.cake_lettering);
        ArrayAdapter<String> cakeLetteringAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cake_lettering);
        spnLettering.setAdapter(cakeLetteringAdapter);
        spnLettering.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // 레터링 선택에 따른 처리
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // 아무것도 선택되지 않았을 때 처리할 내용
            }
        });

        spnLetterShape = findViewById(R.id.spn_letter_shape);
        String[] cake_letter_shapes = getResources().getStringArray(R.array.cake_letter_shape);
        ArrayAdapter<String> cakeLetterShapeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cake_letter_shapes);
        spnLetterShape.setAdapter(cakeLetterShapeAdapter);
        spnLetterShape.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // 글씨체 선택에 따른 처리
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // 아무것도 선택되지 않았을 때 처리할 내용
            }
        });
    }
}