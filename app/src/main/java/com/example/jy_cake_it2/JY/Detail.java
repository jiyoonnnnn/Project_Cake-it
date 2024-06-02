package com.example.jy_cake_it2.JY;

import java.util.List;

public class Detail {
    private String subject;
    private String content;
    private String cakeType;
    private String cakeShape;
    private String cakeColor;
    private String cakeFlavor;
    private String pickupDate;
    private String lettering;
    private int shopId;

    private String modify_date;
//    private String voter;
//    public int getId() { return id; }
    public Detail(String subject, String content, String cakeType, String cakeShape, String cakeColor, String cakeFlavor, String pickupDate, String lettering, int shopId) {
        this.subject = subject;
        this.content = content;
        this.cakeType = cakeType;
        this.cakeShape = cakeShape;
        this.cakeColor = cakeColor;
        this.cakeFlavor = cakeFlavor;
        this.pickupDate = pickupDate;
        this.lettering = lettering;
        this.shopId = shopId;
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

    public String getCakeType() {
        return cakeType;
    }

    public void setCakeType(String cakeType) {
        this.cakeType = cakeType;
    }

    public String getCakeShape() {
        return cakeShape;
    }

    public void setCakeShape(String cakeShape) {
        this.cakeShape = cakeShape;
    }

    public String getCakeColor() {
        return cakeColor;
    }

    public void setCakeColor(String cakeColor) {
        this.cakeColor = cakeColor;
    }

    public String getCakeFlavor() {
        return cakeFlavor;
    }

    public void setCakeFlavor(String cakeFlavor) {
        this.cakeFlavor = cakeFlavor;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getLettering() {
        return lettering;
    }

    public void setLettering(String lettering) {
        this.lettering = lettering;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }
}

