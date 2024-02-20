package com.example.springbootchat.controller;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;


@Controller
@CrossOrigin
public class MaintainYourLogin {
    @Resource
    RedisTemplate<String, String> redisTemplate;

    @RequestMapping("/MaintainYourLogin")
    @ResponseBody
    public String maintainYourLogin(String id) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = "user_" + id;
        String value = valueOperations.get(key);
        if (value == null) {
            return null;
        } else {
            valueOperations.set(key, "1", 10, TimeUnit.SECONDS);
            return null;
        }
    }
}
