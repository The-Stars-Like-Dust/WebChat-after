package com.example.springbootchat.model.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String userName;
    private String age;

    public User() {
    }

    public User(Integer id, String userName, String age) {
        this.id = id;
        this.userName = userName;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
