package com.example.jy_cake_it2.JY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import com.naver.maps.map.LocationSource;

public class MyLocationSource implements LocationSource {

    private final LocationManager locationManager;
    private OnLocationChangedListener listener;
    private final Context context;
    private Location lastKnownLocation; // 마지막 알려진 위치 저장

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            lastKnownLocation = location; // 위치 업데이트 시 저장
            if (listener != null) {
                listener.onLocationChanged(location);
            }
        }

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

    public MyLocationSource(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void activate(OnLocationChangedListener listener) {
        this.listener = listener;
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 경우 실행되지 않음
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
    }

    @Override
    public void deactivate() {
        locationManager.removeUpdates(locationListener);
    }

    public Location getLastKnownLocation() {
        return lastKnownLocation; // 마지막 알려진 위치 반환
    }
}