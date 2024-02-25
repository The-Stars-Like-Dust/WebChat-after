package com.example.springbootchat.service.userService;

import com.example.springbootchat.model.mapperInterface.UserMapper;
import com.example.springbootchat.service.smsService.SMSService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.HashMap;

@Transactional
@Service
public class UserInsertService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private SMSService smsService;


    public String insertUser(String username, String password, String phone) {
        if (username == null || password == null) {
            return "stringError";
        }
        if (username.length() > 20 || password.length() > 20 || username.length() < 6 || password.length() < 6) {
            return "lengthError";
        }
        if (userMapper.selectUserByName(username) != null) {
            return "existError";
        }
        HashMap<Object, Object> map1 = new HashMap<>();
        map1.put("userName", username);
        map1.put("password", password);
        map1.put("age", null);
        userMapper.insertUser(map1);
        BigInteger id = (BigInteger) map1.get("id");
        smsService.setPhoneUser(phone, id.intValue());
        return "success";
    }

}
