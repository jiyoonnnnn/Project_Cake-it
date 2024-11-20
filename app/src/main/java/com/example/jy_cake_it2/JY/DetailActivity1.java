package com.example.jy_cake_it2.JY;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jy_cake_it2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DetailActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail); // activity_detail 레이아웃 파일이 필요합니다.

        // Intent로부터 가게 정보를 받음
        Intent intent = getIntent();
        String shopname = intent.getStringExtra("shopname");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String address = intent.getStringExtra("address");
        String sns = intent.getStringExtra("sns");
        String intro = intent.getStringExtra("intro");

        // UI 요소에 데이터 설정
        TextView shopnameTextView = findViewById(R.id.text_shopname);
        TextView emailTextView = findViewById(R.id.text_email);
        TextView phoneTextView = findViewById(R.id.text_phone);
        TextView addressTextView = findViewById(R.id.text_address);
        TextView snsTextView = findViewById(R.id.text_sns);
        TextView introTextView = findViewById(R.id.text_intro);

        // 각 레이아웃 요소
        LinearLayout layoutShopname = findViewById(R.id.layout_shopname);
        LinearLayout layoutEmail = findViewById(R.id.layout_email);
        LinearLayout layoutPhone = findViewById(R.id.layout_phone);
        LinearLayout layoutAddress = findViewById(R.id.layout_address);
        LinearLayout layoutSns = findViewById(R.id.layout_sns);
        LinearLayout layoutIntro = findViewById(R.id.layout_intro);

        // 가게 이름
        if (shopname != null && !shopname.isEmpty()) {
            shopnameTextView.setText(shopname);
        } else {
            layoutShopname.setVisibility(View.GONE);
        }

        // 이메일
        if (email != null && !email.isEmpty()) {
            emailTextView.setText(email);
        } else {
            layoutEmail.setVisibility(View.GONE);
        }

        // 전화번호
        if (phone != null && !phone.isEmpty()) {
            phoneTextView.setText(phone);
        } else {
            layoutPhone.setVisibility(View.GONE);
        }

        // 주소
        if (address != null && !address.isEmpty()) {
            addressTextView.setText(address);
        } else {
            layoutAddress.setVisibility(View.GONE);
        }

        // SNS
        if (sns != null && !sns.isEmpty()) {
            snsTextView.setText(sns);
        } else {
            layoutSns.setVisibility(View.GONE);
        }

        // 소개
        if (intro != null && !intro.isEmpty()) {
            introTextView.setText(intro);
        } else {
            layoutIntro.setVisibility(View.GONE);
        }

    }
}