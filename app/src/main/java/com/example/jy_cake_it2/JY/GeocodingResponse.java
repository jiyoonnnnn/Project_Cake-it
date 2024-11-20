package com.example.jy_cake_it2.JY;

import java.util.List;

public class GeocodingResponse {
    public String status;
    public Meta meta;
    public List<Address> addresses;

    public static class Meta {
        public int totalCount;
    }

    public static class Address {
        public String roadAddress;
        public String jibunAddress;
        public String x; // 경도
        public String y; // 위도
    }
}