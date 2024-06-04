package com.example.jy_cake_it2.JY;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jy_cake_it2.R;

public class activity_draw_cake extends AppCompatActivity {

    TextView mainText1, mainText2;
    Button btnOrder, btnGetPicture, btn_ai;
    Spinner spnColor, spnShape, spnTopping, spnLettering, spnTaste, spnLetterShape;
    ImageView topping;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_cake);

        // TextView 초기화
        mainText1 = findViewById(R.id.main_text1);
        mainText2 = findViewById(R.id.main_text2);

        // Button 초기화
        //주문하기->입찰, 가게에서 주문 activity로 넘어가야함
        btnOrder = findViewById(R.id.btn_order);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //핸드폰 갤러리/마이페이지에 저장해놓은 도안 가져오기
        btnGetPicture = findViewById(R.id.btn_get_picture);
        btnGetPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //Ai 이미지 생성, 저장 액티비티로 이동// 이동한 액티비티에서 뒤로가기 버튼 구현 필요
        btn_ai = findViewById(R.id.btn_ai);
        btn_ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_draw_cake.this, AI_image_generate.class);
                startActivity(intent);
            }
        });
        // Spinner 초기화
        //sliding up view로 구현 해서 늘였다 줄였다 가능하게 할 것
        //이미지 색 고를때 마다 바뀌도록

        spnColor = findViewById(R.id.spn_color);
        String[] cake_colors = getResources().getStringArray(R.array.cake_color);
        ArrayAdapter<String> cakeColorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,cake_colors);
        cakeColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnColor.setAdapter(cakeColorAdapter);
        spnColor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int color, long l) {
                switch (color){
                    case 0:
                        findViewById(R.id.circle).setBackgroundColor(getResources().getColor(R.color.white,getTheme()));
                        findViewById(R.id.rectangle).setBackgroundColor(getResources().getColor(R.color.white,getTheme()));
                        break;
                    case 1:
                        findViewById(R.id.circle).setBackgroundColor(getResources().getColor(R.color.red,getTheme()));
                        findViewById(R.id.rectangle).setBackgroundColor(getResources().getColor(R.color.red,getTheme()));
                        break;
                    case 2:
                        findViewById(R.id.circle).setBackgroundColor(getResources().getColor(R.color.light_pink,getTheme()));
                        findViewById(R.id.rectangle).setBackgroundColor(getResources().getColor(R.color.light_pink,getTheme()));
                        break;
                    case 3:
                        findViewById(R.id.circle).setBackgroundColor(getResources().getColor(R.color.light_blue_A200,getTheme()));
                        findViewById(R.id.rectangle).setBackgroundColor(getResources().getColor(R.color.light_blue_A200,getTheme()));
                        break;
                    case 4:
                        findViewById(R.id.circle).setBackgroundColor(getResources().getColor(R.color.yellow,getTheme()));
                        findViewById(R.id.rectangle).setBackgroundColor(getResources().getColor(R.color.yellow,getTheme()));
                        break;
                    case 5:
                        findViewById(R.id.circle).setBackgroundColor(getResources().getColor(R.color.black,getTheme()));
                        findViewById(R.id.rectangle).setBackgroundColor(getResources().getColor(R.color.black,getTheme()));
                        break;
                }

            }
            public void onNothingSelected(AdapterView<?> adapterView) {
                // 아무것도 선택되지 않았을 때 처리할 내용
                findViewById(R.id.circle).setBackgroundColor(getResources().getColor(R.color.white,null));
                findViewById(R.id.rectangle).setBackgroundColor(getResources().getColor(R.color.white,null));
            }

        });
        //모양이 바뀌어야 함 다른 옵션도 반영
        spnShape = findViewById(R.id.spn_shape);
        String[] cake_shapes = getResources().getStringArray(R.array.cake_shape);
        ArrayAdapter<String> cakeShapeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cake_shapes);
        cakeShapeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnShape.setAdapter(cakeShapeAdapter);
        spnShape.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        // 원 모양 선택 시 circle View를 표시
                        findViewById(R.id.circle).setVisibility(View.VISIBLE);
                        findViewById(R.id.rectangle).setVisibility(View.GONE);
                        break;
                    case 1:
                        // 사각형 모양 선택 시 rectangle View를 표시
                        findViewById(R.id.circle).setVisibility(View.GONE);
                        findViewById(R.id.rectangle).setVisibility(View.VISIBLE);
                        break;
                    // 여기에 추가적인 도형에 대한 case 추가 가능
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // 아무것도 선택되지 않았을 때 처리할 내용
                findViewById(R.id.circle).setVisibility(View.VISIBLE);
                findViewById(R.id.rectangle).setVisibility(View.GONE);
            }
        });
        //토핑이 이미지 가운데에 등장, 터치해서 확대, 각도등 조절 가능하게 한다
        topping = findViewById(R.id.topping);
        spnTopping = findViewById(R.id.spn_topping);
        String[] cake_toppings = getResources().getStringArray(R.array.cake_topping);
        ArrayAdapter<String> cakeToppingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,cake_toppings);
        cakeToppingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTopping.setAdapter(cakeToppingAdapter);
        spnTopping.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
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
        });
        //손으로 직접 그려서 판단하게 하는 ai모델을 구축하거나 해당 글자를 아래의 글씨체 모양으로 화면에 띄워주어 크기 조절, 각도 조절등이 가능하게 한다.
        spnLettering = findViewById(R.id.spn_lettering);
        String[] cake_lettering = getResources().getStringArray(R.array.cake_lettering);
        ArrayAdapter<String> cakeLetteringAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,cake_lettering);
        cakeLetteringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLettering.setAdapter(cakeLetteringAdapter);
        spnLettering.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
//        spnTaste = findViewById(R.id.spn_taste);
//        String[] cake_taste = getResources().getStringArray(R.array.cake_taste);
//        ArrayAdapter<String> cakeTasteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,cake_taste);
//        cakeTasteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnTaste.setAdapter(cakeTasteAdapter);
//        spnTaste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });
        //글씨체 수정시 위의 레터도 같이 수정되게
        spnLetterShape = findViewById(R.id.spn_letter_shape);
        String[] cake_letter_shape = getResources().getStringArray(R.array.cake_letter_shape);
        ArrayAdapter<String> cakeLetterShapeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,cake_letter_shape);
        cakeLetterShapeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLetterShape.setAdapter(cakeLetterShapeAdapter);
        spnLetterShape.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

}