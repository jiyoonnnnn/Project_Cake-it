package com.example.jy_cake_it2.JY;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.jy_cake_it2.R;
import com.google.android.material.navigation.NavigationView;

public class activity_browse extends AppCompatActivity {
    private final int Fragment_1 = 1;
    private final int Fragment_2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_browse);
        TextView shoplist = findViewById(R.id.shoplist);
        TextView designlist = findViewById(R.id.designlist);
        NavigationView navigationView;
        Fragment shop_list, design_list;
        findViewById(R.id.shoplist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoplist.setTextColor(Color.parseColor("#000000"));
                designlist.setTextColor(Color.parseColor("#FFFFFF"));
                FragmentView(Fragment_1);

            }
        });

        findViewById(R.id.designlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoplist.setTextColor(Color.parseColor("#FFFFFF"));
                designlist.setTextColor(Color.parseColor("#000000"));
                FragmentView(Fragment_2);


            }
        });
        FragmentView(Fragment_1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void FragmentView(int fragment){

        //FragmentTransactiom를 이용해 프래그먼트를 사용합니다.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment){
            case 1:
                // 첫번 째 프래그먼트 호출
                Shop_list fragment1 = new Shop_list();
                transaction.replace(R.id.fragment_container, fragment1);
                transaction.commit();
                break;

            case 2:
                // 두번 째 프래그먼트 호출
                Design_list fragment2 = new Design_list();
                transaction.replace(R.id.fragment_container, fragment2);
                transaction.commit();
                break;
        }
        TextView btn1;
        btn1 = findViewById(R.id.back);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_browse.this, Login_main.class);
                startActivity(intent);
            }
        });
        TextView btn2;
        btn2 = findViewById(R.id.myOrder);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_browse.this, user_order.class);
                startActivity(intent);
            }
        });
    }
}