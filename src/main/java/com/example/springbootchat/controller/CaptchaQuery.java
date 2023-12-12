package com.example.springbootchat.controller;

import cn.hutool.captcha.LineCaptcha;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin
@Controller()
@RequestMapping("/captcha")
public class CaptchaQuery {
    @Resource(name = "lineCaptcha")
    private LineCaptcha lineCaptcha;

    @GetMapping("/get")
    public void getCaptcha(ServletOutputStream outputStream) {
        lineCaptcha.createCode();
        lineCaptcha.write(outputStream);
    }

    @GetMapping("/verify")
    public boolean verifyCaptcha(String code) {
        return lineCaptcha.verify(code);
    }
}
