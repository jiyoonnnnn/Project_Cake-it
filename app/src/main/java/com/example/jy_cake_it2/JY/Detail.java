package com.example.jy_cake_it2.JY;

import java.util.List;

public class Detail {
    private String subject;
    private String content;
    private String create_date;
    private User user;
    private String modify_date;
    private String cake_type;
    private String cake_shape;
    private String cake_color;
    private String cake_flavor;
    private String pickup_date;
    private String lettering;
    private int shop_id;

//    private String voter;
//    public int getId() { return id; }
    public Detail(String subject, String content, String cake_type, String cake_shape, String cake_color, String cake_flavor, String pickup_date, String lettering, int shop_id) {
        this.subject = subject;
        this.content = content;
        this.cake_type = cake_type;
        this.cake_shape = cake_shape;
        this.cake_color = cake_color;
        this.cake_flavor = cake_flavor;
        this.pickup_date = pickup_date;
        this.lettering = lettering;
        this.shop_id = shop_id;
    }
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getCreateDate() {
        return create_date;
    }
    public User getUser() {
        return user;
    }
    public String getModifyDate() {
        return modify_date;
    }
    public String getCakeType() {
        return cake_type;
    }

    public void setCakeType(String cake_type) {
        this.cake_type = cake_type;
    }

    public String getCakeShape() {
        return cake_shape;
    }

    public void setCakeShape(String cake_shape) {
        this.cake_shape = cake_shape;
    }

    public String getCakeColor() {
        return cake_color;
    }

    public void setCakeColor(String cake_color) {
        this.cake_color = cake_color;
    }

    public String getCakeFlavor() {
        return cake_flavor;
    }

    public void setCakeFlavor(String cake_flavor) {
        this.cake_flavor = cake_flavor;
    }

    public String getPickupDate() {
        return pickup_date;
    }

    public void setPickupDate(String pickup_date) {
        this.pickup_date = pickup_date;
    }

    public String getLettering() {
        return lettering;
    }

    public void setLettering(String lettering) {
        this.lettering = lettering;
    }

    public int getShopId() {
        return shop_id;
    }

    public void setShopId(int shop_id) {
        this.shop_id = shop_id;
    }
}

