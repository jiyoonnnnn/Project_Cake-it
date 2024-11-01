package com.example.jy_cake_it2.JY;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jy_cake_it2.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//public class Design_list extends Fragment {
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Layout 파일 연결
//        return inflater.inflate(R.layout.fragment_design_list, container, false);
//    }
//}
public class Design_list extends Fragment implements OnMapReadyCallback {
    private NaverMap naverMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_design_list, container, false);
        initializeMap(rootView);
        locationSource = new FusedLocationSource(requireActivity(), LOCATION_PERMISSION_REQUEST_CODE);
        return rootView;
    }

    private void initializeMap(View rootView) {
        FragmentManager fm = getChildFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true); // 기본 위치 버튼 활성화

        loadShopDataAndCreateMarkers();
    }

    private void loadShopDataAndCreateMarkers() {
        LoginApiService apiService = RetrofitClient.getApiService(); // RetrofitClient 사용
        Call<ApiResponse> call = apiService.getShops();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Shop> shopList = response.body().getShop_list();
                    for (Shop shop : shopList) {
                        try {
                            double locX = Double.parseDouble(shop.getLocX());
                            double locY = Double.parseDouble(shop.getLocY());
                            LatLng shopLocation = new LatLng(locX, locY);

                            MarkerInfo markerInfo = new MarkerInfo(shop.getShopname(), shop.getIntro(), shop);
                            Marker marker = new Marker();
                            marker.setPosition(shopLocation);
                            marker.setMap(naverMap);
                            marker.setTag(markerInfo);

                            marker.setOnClickListener(overlay -> {
                                Marker clickedMarker = (Marker) overlay;
                                MarkerInfo info = (MarkerInfo) clickedMarker.getTag();

                                Intent intent = new Intent(getActivity(), DetailActivity1.class);
                                intent.putExtra("shopname", info.getShop().getShopname());
                                intent.putExtra("email", info.getShop().getEmail());
                                intent.putExtra("phone", info.getShop().getPhone());
                                intent.putExtra("address", info.getShop().getAddress());
                                intent.putExtra("sns", info.getShop().getSns());
                                intent.putExtra("intro", info.getShop().getIntro());
                                startActivity(intent);

                                return true;
                            });
                        } catch (NumberFormatException e) {
                            e.printStackTrace(); // 좌표 변환 오류 처리
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                // 실패 처리
            }
        });
    }

    static class MarkerInfo {
        private final String title;
        private final String description;
        private final Shop shop;

        public MarkerInfo(String title, String description, Shop shop) {
            this.title = title;
            this.description = description;
            this.shop = shop;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public Shop getShop() {
            return shop;
        }
    }
}