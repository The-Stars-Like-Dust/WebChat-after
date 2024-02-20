package com.example.springbootchat.service.messagesService;

import com.example.springbootchat.model.mapperInterface.MessageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Transactional
@Service
public class MessageInsertService {
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private SimpleDateFormat MseeagesSimpleDateFormat;


    public String insertMessage(String text, Integer id1, Integer id2) {
        if (text == null && text.trim().isEmpty()) {
            return "stringError";
        }
        if (text.length() > 500 || text.isEmpty()) {
            return "lengthError";
        }
        try {
            HashMap<Object, Object> map1 = new HashMap<>();
            map1.put("message", text);
            map1.put("pid", id1);
            map1.put("vid", id2);
            map1.put("datetime", MseeagesSimpleDateFormat.format(new Date()));
            messageMapper.insertMessage(map1);
        } catch (Exception e) {
            return "error";
        }
        return "success";
    }

    public String deleteFriend(Integer id1, Integer id2) {
        messageMapper.deleteMessage(id1, id2);
        return "success";
    }


}
