package com.example.jy_cake_it2.JY;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jy_cake_it2.R;

public class DetailActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView detailText = findViewById(R.id.text_detail);
        detailText.setText("2: 공대건물");
    }
}