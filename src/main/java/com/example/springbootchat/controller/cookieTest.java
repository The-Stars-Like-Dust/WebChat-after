package com.example.springbootchat.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://192.168.138.1:63342/", "http://localhost:8081/"}, allowCredentials = "true")
public class cookieTest {
    @PostMapping("/cookie")
    public String cookieTest(HttpSession session) {
        Object attribute = session.getAttribute("user");
        if (attribute == null) {
            session.setAttribute("user", "123");
            return "未访问过";
        } else {
            return "访问过了";
        }
    }
}
