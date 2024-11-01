package com.example.jy_cake_it2.JY;

public class OrderRequest {
    private int question_id;

    public OrderRequest(int question_id) {
        this.question_id = question_id;
    }

    public int getQuestionId() {
        return question_id;
    }

    public void setQuestionId(int question_id) {
        this.question_id = question_id;
    }
}
