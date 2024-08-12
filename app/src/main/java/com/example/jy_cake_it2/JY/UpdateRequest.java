package com.example.jy_cake_it2.JY;

public class UpdateRequest {
    private int question_id;
    private int shop_id;
    public UpdateRequest(int question_id, int shop_id) {
        this.question_id = question_id;
        this.shop_id = shop_id;
    }

    // Getter and Setter methods
    public int getQuestionId() {
        return question_id;
    }

    public void setQuestionId(int question_id) {
        this.question_id = question_id;
    }

    public int getShopId() {
        return shop_id;
    }

    public void setShopId(int shop_id) {
        this.shop_id = shop_id;
    }
}
