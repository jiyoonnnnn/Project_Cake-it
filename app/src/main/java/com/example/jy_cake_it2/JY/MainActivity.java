package com.example.jy_cake_it2.JY;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private MyLocationSource myLocationSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_map);

        myLocationSource = new MyLocationSource(this);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        initializeMap();
    }

    // 지도 초기화 메소드
    private void initializeMap() {
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
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
        naverMap.setLocationSource(myLocationSource);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, LOCATION_PERMISSION_REQUEST_CODE);

        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);

        LatLng coord1 = new LatLng(36.6521, 127.4949);
        LatLng coord2 = new LatLng(36.6520, 127.49701);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        addMarker(coord1, new MarkerInfo("1빵", "도서관", DetailActivity1.class));
        addMarker(coord2, new MarkerInfo("2빵", "공대건물", DetailActivity2.class));
    }

    // 마커 추가 메소드
    private void addMarker(LatLng position, MarkerInfo markerInfo) {
        Marker marker = new Marker();
        marker.setPosition(position);
        marker.setMap(naverMap);
        marker.setTag(markerInfo);

        marker.setOnClickListener(overlay -> {
            Marker clickedMarker = (Marker) overlay;
            MarkerInfo info = (MarkerInfo) clickedMarker.getTag();

            Intent intent = new Intent(MainActivity.this, info.getDetailActivityClass());
            startActivity(intent);
            return true;
        });
    }

    // 마커 정보 클래스
    static class MarkerInfo {
        private final String title;
        private final String description;
        private final Class<?> detailActivityClass;

        public MarkerInfo(String title, String description, Class<?> detailActivityClass) {
            this.title = title;
            this.description = description;
            this.detailActivityClass = detailActivityClass;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public Class<?> getDetailActivityClass() {
            return detailActivityClass;
        }
    }
}