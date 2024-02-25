package com.example.springbootchat;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.example.springbootchat.model.mapperInterface")
@EnableScheduling
public class SpringbootChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootChatApplication.class, args);
    }
}
