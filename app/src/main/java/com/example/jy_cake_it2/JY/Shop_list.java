package com.example.jy_cake_it2.JY;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jy_cake_it2.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link Shop_list#newInstance} factory method to
// * create an instance of this fragment.
// */
public class Shop_list extends Fragment {


//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

//    public Shop_list() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment Shop_list.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static Shop_list newInstance(String param1, String param2) {
//        Shop_list fragment = new Shop_list();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
//        return inflater.inflate(R.layout.fragment_shop_list, container, false);
        TextView shopList, shopList2, shopList3;
//        shopList = findViewById(R.id.browse);

        View rootView = inflater.inflate(R.layout.fragment_shop_list, container, false);

        // findViewById를 사용하여 뷰 참조
        shopList2 = rootView.findViewById(R.id.browse2); // 프래그먼트 뷰에서 찾기
        shopList3 = rootView.findViewById(R.id.browse3); // 프래그먼트 뷰에서 찾기
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://132.145.80.50:9999/") // 기본 URL만 입력
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginApiService service = retrofit.create(LoginApiService.class);

        Call<ApiResponse> call = service.getShops();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                shopList3.setText("code = " + response.code());
                if (response.isSuccessful()) {
//                    shopList3.setText(response.body());

                    ApiResponse apiResponse = response.body();
                    List<Shop> shops = apiResponse.getShop_list();
                    for (Shop shop : shops) {
//                        shopList3.setText(shop.toString());


//                            shopList3.setText("번호: " + shops.getShop_list() + "\n");

                        String content = "";
                        content += "번호: " + shop.getId() + "\n";
                        content += "상호명: " + shop.getShopname() + "\n";
                        content += "주소: " + shop.getAddress() + "\n";
                        content += "사장님: " + shop.getUsername() + "\n" + "\n";


                        shopList2.append(content);

                    }
                } else {
                    Toast.makeText(requireContext(), "망해따", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Failed to fetch shops", Toast.LENGTH_SHORT).show();
                shopList3.setText(t.getMessage());
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_shop_list, container, false);
//    }
}