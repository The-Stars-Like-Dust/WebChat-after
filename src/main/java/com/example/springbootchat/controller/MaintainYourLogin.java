package com.example.springbootchat.controller;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

/**
 * 这是一个控制器类，用于维护用户的登录状态。
 * 它使用Redis作为存储，当用户的登录状态被验证时，会重置其在Redis中的过期时间。
 */
@Controller
@CrossOrigin(origins = {"https://lt.ximuliunian.top", "http://192.168.138.1:5500"}, allowCredentials = "true")
public class MaintainYourLogin {
    // 注入RedisTemplate，用于操作Redis
    @Resource
    RedisTemplate<String, String> redisTemplate;

    /**
     * 此方法用于维护用户的登录状态。
     * 它会检查Redis中是否存在用户的记录，如果存在，则重置其过期时间。
     * 如果不存在，则返回null，表示用户未登录或登录已过期。
     *
     * @param id 用户的ID
     * @return 如果用户未登录或登录已过期，返回null，否则也返回null（实际上，这个方法的返回值并没有实际用途，可能是设计上的问题）
     */
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