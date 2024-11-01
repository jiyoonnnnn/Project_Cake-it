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
    private MyLocationSource myLocationSource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_design_list, container, false);

        myLocationSource = new MyLocationSource(requireContext());

        initializeMap(rootView); // 지도 초기화

        locationSource = new FusedLocationSource(requireActivity(), LOCATION_PERMISSION_REQUEST_CODE);

        return rootView;
    }

    private void initializeMap(View rootView) {
        FragmentManager fm = getChildFragmentManager(); // Fragment 내에서 FragmentManager를 사용해야 함
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
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);

        naverMap.setLocationSource(myLocationSource);

        ActivityCompat.requestPermissions(requireActivity(), new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);

        LatLng coord = new LatLng(36.6521, 127.4949);
        LatLng coord2 = new LatLng(36.6520, 127.49701);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        MainActivity.MarkerInfo markerInfo1 = new MainActivity.MarkerInfo("1빵", "도서관", DetailActivity1.class);
        Marker marker1 = new Marker();
        marker1.setPosition(coord);
        marker1.setMap(naverMap);
        marker1.setTag(markerInfo1);

        MainActivity.MarkerInfo markerInfo2 = new MainActivity.MarkerInfo("2빵", "공대건물", DetailActivity2.class);
        Marker marker2 = new Marker();
        marker2.setPosition(coord2);
        marker2.setMap(naverMap);
        marker2.setTag(markerInfo2);

        Overlay.OnClickListener listener = overlay -> {
            Marker marker = (Marker) overlay;
            MainActivity.MarkerInfo info = (MainActivity.MarkerInfo) marker.getTag();

            Intent intent = new Intent(getActivity(), info.getDetailActivityClass());
            startActivity(intent);

            return true; // 마커 클릭 이벤트 소비
        };

        marker1.setOnClickListener(listener);
        marker2.setOnClickListener(listener);
    }
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
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public Design_list() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment Design_list.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static Design_list newInstance(String param1, String param2) {
//        Design_list fragment = new Design_list();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
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
//        return inflater.inflate(R.layout.fragment_design_list, container, false);
//    }
}