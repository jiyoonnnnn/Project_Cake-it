package com.example.jy_cake_it2.JY;

public class DenyRequest {
    private int question_id;
    private String status_msg;

    public DenyRequest(int question_id, String status_msg) {
        this.question_id = question_id; this.status_msg = status_msg;
    }

    public int getQuestionId() {
        return question_id;
    }

    public void setQuestionId(int question_id) {
        this.question_id = question_id;
    }
}
