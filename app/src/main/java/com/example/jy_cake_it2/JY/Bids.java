package com.example.jy_cake_it2.JY;

public class Bids {
    private int id;
    private int content;
    private String create_date;

    private Shop user;
    private int questionId;
    private String modifyDate;
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
