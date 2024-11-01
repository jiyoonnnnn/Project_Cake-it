package com.example.jy_cake_it2.JY;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.jy_cake_it2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class activity_browse extends AppCompatActivity {
    private final int Fragment_1 = 1;
    private final int Fragment_2 = 2;
    private Shop_list shopFragment = new Shop_list();
    private Design_list designFragment = new Design_list();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_browse);

        TextView shoplist = findViewById(R.id.shoplist);
        TextView designlist = findViewById(R.id.designlist);
//        NavigationView navigationView;
//        Fragment shop_list, design_list;

//        Intent intent = getIntent();
//        int detailId = intent.getIntExtra("DETAIL_ID", -1);

        shoplist.setOnClickListener(new View.OnClickListener(){
        //findViewById(R.id.shoplist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoplist.setTextColor(Color.parseColor("#000000"));
                designlist.setTextColor(Color.parseColor("#FFFFFF"));
                FragmentView(Fragment_1);

            }
        });
        designlist.setOnClickListener(new View.OnClickListener() {

        //(R.id.designlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoplist.setTextColor(Color.parseColor("#FFFFFF"));
                designlist.setTextColor(Color.parseColor("#000000"));
                FragmentView(Fragment_2);


            }
        });
        FragmentView(Fragment_1);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_browse);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_draw_cake) {
//                switch (item.getItemId()) {
//                    case R.id.nav_draw_cake:
                    startActivity(new Intent(activity_browse.this, activity_draw_cake.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_browse) {
//                    case R.id.nav_browse:
                    startActivity(new Intent(activity_browse.this, activity_browse.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_order) {
//                    case R.id.nav_order:
                    startActivity(new Intent(activity_browse.this, user_order.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_mypage) {
//                    case R.id.nav_mypage:
                    startActivity(new Intent(activity_browse.this, user_order.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
    private void FragmentView(int fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // 미리 생성된 프래그먼트 사용
        switch (fragment) {
            case Fragment_1:
                transaction.replace(R.id.fragment_container, shopFragment).commit();
                break;
            case Fragment_2:
                transaction.replace(R.id.fragment_container, designFragment).commit();
                break;
        }
    }
}