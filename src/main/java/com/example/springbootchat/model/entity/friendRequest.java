package com.example.springbootchat.model.entity;

import lombok.Data;

@Data
public class friendRequest {
    private User pUsers;
    private User vUser;
    private String requestTime;
    private String firstMessage;

    public User getpUsers() {
        return pUsers;
    }

    public void setpUsers(User pUsers) {
        this.pUsers = pUsers;
    }

    public User getvUser() {
        return vUser;
    }

    public void setvUser(User vUser) {
        this.vUser = vUser;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getFirstMessage() {
        return firstMessage;
    }

    public void setFirstMessage(String firstMessage) {
        this.firstMessage = firstMessage;
    }
}
