package com.example.springbootchat.controller;

import cn.hutool.captcha.LineCaptcha;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 这是一个处理验证码相关请求的控制器类。
 * 它允许客户端获取验证码并进行验证。
 * 它支持来自指定源的跨源请求。
 */
@CrossOrigin(origins = {"https://lt.ximuliunian.top", "http://192.168.138.1:5500"}, allowCredentials = "true")
@Controller()
@RequestMapping("/captcha")
public class CaptchaQuery {
    /**
     * 此方法生成验证码并将其写入输出流。
     * 如果会话中不存在验证码，它将创建一个新的验证码。
     *
     * @param outputStream 要将验证码写入的输出流。
     * @param session      当前的HTTP会话。
     */
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

    /**
     * 此方法验证客户端提供的验证码代码。
     * 如果会话中不存在验证码或代码不正确，它将返回false。
     *
     * @param code    客户端提供的验证码代码。
     * @param session 当前的HTTP会话。
     * @return 如果验证码代码正确，则返回true，否则返回false。
     */
    @GetMapping("/verify/{code}")
    @ResponseBody
    public boolean verifyCaptcha(@PathVariable("code") String code, HttpSession session) {
        LineCaptcha lineCaptcha = (LineCaptcha) session.getAttribute("lineCaptcha");
        if (lineCaptcha == null) {
            return false;
        }
        Boolean verify = lineCaptcha.verify(code);
        return verify;

    }
}