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
    private int question_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_shop_list, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        shopAdapter = new ShopAdapter(new ArrayList<>(), this::updateShopDetail);
        recyclerView.setAdapter(shopAdapter);

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

    private void updateShopDetail(Shop shop) {
        int shop_id = shop.getId();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("AccessToken", null);

        if (accessToken != null) {
            Retrofit retrofit = RetrofitClient.getClient(accessToken);
            LoginApiService apiService = retrofit.create(LoginApiService.class);
            Call<ApiResponse> call = apiService.getUserOrders("Bearer " + accessToken);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse != null && apiResponse.getOrder_list() != null && !apiResponse.getOrder_list().isEmpty()) {
                            question_id = apiResponse.getOrder_list().get(0).getId();

                            UpdateRequest updateRequest = new UpdateRequest(question_id, shop_id);
                            updateShop(accessToken, updateRequest);
                        } else {
                            Toast.makeText(getContext(), "No orders found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to fetch orders", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Error fetching orders: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Access Token found", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateShop(String accessToken, UpdateRequest updateRequest) {
        Retrofit retrofit = RetrofitClient.getClient(accessToken);
        LoginApiService apiService = retrofit.create(LoginApiService.class);

        Call<UpdateRequest> call = apiService.updateShop("Bearer " + accessToken, updateRequest);
        call.enqueue(new Callback<UpdateRequest>() {
            @Override
            public void onResponse(Call<UpdateRequest> call, Response<UpdateRequest> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Shop updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), bid_user.class);
                    intent.putExtra("DETAIL_ID", updateRequest.getQuestionId());
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "업데이트 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateRequest> call, Throwable t) {
                Toast.makeText(getContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}