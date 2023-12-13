package com.example.springbootchat.model.entity;

import lombok.Data;

@Data
public class Message {
    User user1;
    User user2;
    String text;
    String time;

    public Message() {
    }

    public Message(User user1, User user2, String text, String time) {
        this.user1 = user1;
        this.user2 = user2;
        this.text = text;
        this.time = time;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
