package com.example.springbootchat;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.example.springbootchat.model.mapperInterface")
public class SpringbootChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootChatApplication.class, args);

    }

    @Bean(name = "lineCaptcha")
    public LineCaptcha createLineCaptcha() {
        return CaptchaUtil.createLineCaptcha(150, 70, 5, 300);
    }

}
