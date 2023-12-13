package com.example.springbootchat.controller;

import cn.hutool.captcha.LineCaptcha;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Controller()
@RequestMapping("/captcha")
public class CaptchaQuery {
    @GetMapping("/get")
    public void getCaptcha(ServletOutputStream outputStream, HttpSession session) {
        LineCaptcha lineCaptcha = (LineCaptcha) session.getAttribute("lineCaptcha");
        if (lineCaptcha == null) {
            lineCaptcha = new LineCaptcha(150, 70, 5, 300);
            session.setAttribute("lineCaptcha", lineCaptcha);
        }
        lineCaptcha.createCode();
        lineCaptcha.write(outputStream);
    }

    @GetMapping("/verify/{code}")
    @ResponseBody
    public boolean verifyCaptcha(@PathVariable("code") String code, HttpSession session) {
        LineCaptcha lineCaptcha = (LineCaptcha) session.getAttribute("lineCaptcha");
        if (lineCaptcha == null) {
            return false;
        }
        return lineCaptcha.verify(code);
    }
}
