package com.example.springbootchat;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.springbootchat.model.mapperInterface")
public class SpringbootChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootChatApplication.class, args);

    }

}
