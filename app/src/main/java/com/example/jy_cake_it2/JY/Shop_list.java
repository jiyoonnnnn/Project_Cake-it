package com.example.jy_cake_it2.JY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jy_cake_it2.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Shop_list extends Fragment {
    private RecyclerView recyclerView;
    private ShopAdapter shopAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_shop_list, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Adapter 설정, 가게 클릭 시 updateShopDetail 메서드 호출
        shopAdapter = new ShopAdapter(new ArrayList<>(), this::updateShopDetail);
        recyclerView.setAdapter(shopAdapter);

        // 가게 리스트 가져오기
        fetchShopList();

        return rootView;
    }

    private void fetchShopList() {
        LoginApiService apiService = RetrofitClient.getApiService();
        Call<ApiResponse> call = apiService.getShops();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    List<Shop> shops = apiResponse.getShop_list();
                    shopAdapter.updateShops(shops);
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch shops", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Error fetching shops: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 가게 클릭 시 상세 페이지로 이동
    private void updateShopDetail(Shop shop) {
        // ShopDetailActivity로 이동할 Intent 생성
        Intent intent = new Intent(getActivity(), user_shop_home.class);
        intent.putExtra("SHOP_ID", shop.getId());// 추가 정보가 있다면 함께 전달

        startActivity(intent);  // Intent 시작
    }
}