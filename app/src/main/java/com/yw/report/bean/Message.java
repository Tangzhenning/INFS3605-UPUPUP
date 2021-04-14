package com.yw.report.bean;
//评论
public class Message {

    private User user;
    private String content;
    private String date;

    public Message(User user, String content, String date) {
        this.user = user;
        this.content = content;
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
