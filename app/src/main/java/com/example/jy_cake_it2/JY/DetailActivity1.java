package com.example.jy_cake_it2.JY;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jy_cake_it2.R;

public class DetailActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView detailText = findViewById(R.id.text_detail);
        detailText.setText("1: 도서관");
    }
}