package com.example.springbootchat.service.userService;

import com.example.springbootchat.model.mapperInterface.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Transactional
@Service
public class UserInsertService {
    @Resource
    private UserMapper userMapper;

    public String insertUser(String username, String password) {
        if (username == null || password == null) {
            return "stringError";
        }
        if (username.length() > 20 || password.length() > 20 || username.length() < 6 || password.length() < 6) {
            return "lengthError";
        }
        if (userMapper.selectUserByName(username) != null) {
            return "existError";
        }
        try {
            HashMap<Object, Object> map1 = new HashMap<>();
            map1.put("userName", username);
            map1.put("password", password);
            map1.put("age", null);
            userMapper.insertUser(map1);
        } catch (Exception e) {
            return "error";
        }
        return "success";
    }

}
