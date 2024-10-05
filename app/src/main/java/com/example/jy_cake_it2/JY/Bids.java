package com.example.jy_cake_it2.JY;

public class Bids {
    private int id;
    private int content;
    private String create_date; // 입찰 날짜
    private Shop user; // 입찰한 판매자 정보
    private int questionId; // 주문 번호 (question_id)
    private String modifyDate; // 입찰 수정 날짜
    private  String comment;
    public Bids(int content, String comment) {
        this.content = content;
        this.comment = comment;
    }
    public int getId() { return id; }
    public int getContent() { return content; }
    public String getCreateDate() { return create_date; }
    public Shop getShop() { return user; }
    public String getComment() { return comment; }
}
