package com.example.jy_cake_it2.JY;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jy_cake_it2.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Shop_list extends Fragment {
    private RecyclerView recyclerView;
    private ShopAdapter shopAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
//        return inflater.inflate(R.layout.fragment_shop_list, container, false);
        TextView shopList, shopList2, shopList3;
//        shopList = findViewById(R.id.browse);

        View rootView = inflater.inflate(R.layout.fragment_shop_list, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        shopAdapter = new ShopAdapter(new ArrayList<Shop>(), new ShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Shop shop) {
                // 가게 클릭 시 수행할 작업 정의
                Toast.makeText(requireContext(), "Clicked: " + shop.getShopname(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(shopAdapter);
        // 가게 목록을 가져와서 어댑터에 연결
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
                    ShopAdapter.OnItemClickListener clickListener = new ShopAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Shop shop) {
                            Intent intent = new Intent(getActivity(),Detail_Post_Exemple.class);
                            // 다른 페이지로 전달할 데이터가 있다면 여기에 추가
                            startActivity(intent);
                        }
                    };
                    shopAdapter = new ShopAdapter(shops, clickListener);
                    recyclerView.setAdapter(shopAdapter);
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
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}