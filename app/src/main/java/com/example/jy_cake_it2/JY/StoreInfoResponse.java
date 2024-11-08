package com.example.jy_cake_it2.JY;

public class StoreInfoResponse {
    private int id;
    private String username;
    private String shopname;
    private String email;
    private String phone;
    private String address;
    private String sns;
    private String intro;
    private String logo;
    private String loc_x;
    private String loc_y;
    private String bank;

    // 기본 생성자
    public StoreInfoResponse() {
    }

    // 모든 필드를 포함하는 생성자
    public StoreInfoResponse(int id, String username, String shopname, String email, String phone,
                             String address, String sns, String intro, String logo,
                             String loc_x, String loc_y, String bank) {
        this.id = id;
        this.username = username;
        this.shopname = shopname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.sns = sns;
        this.intro = intro;
        this.logo = logo;
        this.loc_x = loc_x;
        this.loc_y = loc_y;
        this.bank = bank;
    }

    // Getter 및 Setter 메소드
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSns() {
        return sns;
    }

    public void setSns(String sns) {
        this.sns = sns;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLoc_x() {
        return loc_x;
    }

    public void setLoc_x(String loc_x) {
        this.loc_x = loc_x;
    }

    public String getLoc_y() {
        return loc_y;
    }

    public void setLoc_y(String loc_y) {
        this.loc_y = loc_y;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", shopname='" + shopname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", sns='" + sns + '\'' +
                ", intro='" + intro + '\'' +
                ", logo='" + logo + '\'' +
                ", loc_x='" + loc_x + '\'' +
                ", loc_y='" + loc_y + '\'' +
                ", bank='" + bank + '\'' +
                '}';
    }
}