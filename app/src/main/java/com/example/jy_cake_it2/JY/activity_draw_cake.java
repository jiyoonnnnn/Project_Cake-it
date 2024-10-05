package com.example.jy_cake_it2.JY;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.jy_cake_it2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class activity_draw_cake extends AppCompatActivity {

    TextView mainText1, mainText2, myText, btn1;
    Button btnOrder, btnGetPicture, btn_ai;
    Spinner spnColor, spnShape, spnLetterShape, spnTaste;

    EditText edit_letter;
    ImageView circle, square;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_cake);

        // TextView 초기화
        mainText1 = findViewById(R.id.main_text1);
        mainText2 = findViewById(R.id.main_text2);
        myText = findViewById(R.id.circle_text);

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
                String cake_flavor = spnTaste.getSelectedItem().toString();
                String lettering = edit_letter.getText().toString();
                Intent intent = new Intent(activity_draw_cake.this, activity_set_reservation.class);
                intent.putExtra("cake_type", cake_type);
                intent.putExtra("cake_shape", cake_shape);
                intent.putExtra("cake_color", cake_color);
                intent.putExtra("cake_flavor", cake_flavor);
                intent.putExtra("lettering", lettering);
                startActivity(intent);
            }
        });

        btn_ai = findViewById(R.id.btn_ai);
        btn_ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_draw_cake.this, AI_image_generate.class);
                startActivity(intent);
            }
        });

        btn1 = findViewById(R.id.back);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_draw_cake.this, Login_main.class);
                startActivity(intent);
            }
        });
        circle = findViewById(R.id.circle);
        square = findViewById(R.id.rectangle);
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
                        circle.setImageResource(R.drawable.circle_shape);
                        square.setImageResource(R.drawable.rectangle_shape);
                        break;
                    case 1:
                        circle.setImageResource(R.drawable.circle_color_red);
                        square.setImageResource(R.drawable.rectangle_color_red);
                        break;
                    case 2:
                        circle.setImageResource(R.drawable.circle_color_pink);
                        square.setImageResource(R.drawable.rectangle_color_pink);
                        break;
                    case 3:
                        circle.setImageResource(R.drawable.circle_color_blue);
                        square.setImageResource(R.drawable.rectangle_color_blue);
                        break;
                    case 4:
                        circle.setImageResource(R.drawable.circle_color_yellow);
                        square.setImageResource(R.drawable.rectangle_color_yellow);
                        break;
                    case 5:
                        circle.setImageResource(R.drawable.circle_color_black);
                        square.setImageResource(R.drawable.rectangle_color_black);
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
        edit_letter = findViewById(R.id.edit_lettering);
        spnLetterShape = findViewById(R.id.spn_letter_shape);
        String[] cake_letter_shapes = getResources().getStringArray(R.array.cake_letter_shape);
        ArrayAdapter<String> cakeLetterShapeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cake_letter_shapes);
        spnLetterShape.setAdapter(cakeLetterShapeAdapter);
        spnLetterShape.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // 글씨체 선택에 따른 처리
                Typeface typeface = null;
                switch(position){
                    case 0:
                        typeface = ResourcesCompat.getFont(getApplicationContext(),R.font.english_korean_test_font1);
                        break;
                    case 1:
                        typeface = ResourcesCompat.getFont(getApplicationContext(),R.font.english_korean_test_font2);
                        break;
                    case 2:
                        typeface = ResourcesCompat.getFont(getApplicationContext(),R.font.english_test_font1);
                        break;
                    case 3:
                        typeface = ResourcesCompat.getFont(getApplicationContext(),R.font.english_test_font2);
                        break;
                }
                if (typeface != null) {
                    edit_letter.setTypeface(typeface);
                    myText.setTypeface(typeface);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // 아무것도 선택되지 않았을 때 처리할 내용
            }
        });
        edit_letter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                myText.setText(charSequence); // 추가된 TextView의 텍스트 업데이트
                myText.setVisibility(View.VISIBLE); // 텍스트가 입력되면 TextView 보이게 설정
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        spnTaste = findViewById(R.id.spn_taste);
        String[] cake_taste = getResources().getStringArray(R.array.cake_taste);
        ArrayAdapter<String> cakeTasteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cake_taste);
        spnTaste.setAdapter(cakeTasteAdapter);
        spnTaste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // 맛에 따른 데이터 처리
                switch(position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // 아무것도 선택되지 않았을 때 처리할 내용
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_draw_cake) {
//                switch (item.getItemId()) {
//                    case R.id.nav_draw_cake:
                    startActivity(new Intent(activity_draw_cake.this, activity_draw_cake.class));
                    return true;
                } else if (id == R.id.nav_browse) {
//                    case R.id.nav_browse:
                    startActivity(new Intent(activity_draw_cake.this, activity_browse.class));
                    return true;
                } else if (id == R.id.nav_order) {
//                    case R.id.nav_order:
                    startActivity(new Intent(activity_draw_cake.this, bid_user.class));
                    return true;
                } else if (id == R.id.nav_mypage) {
//                    case R.id.nav_mypage:
                    startActivity(new Intent(activity_draw_cake.this, user_order.class));
                    return true;
                }
                return false;
            }
        });
    }
}