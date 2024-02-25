package com.example.springbootchat.service.messagesService;

import com.example.springbootchat.model.mapperInterface.MessageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 这是一个处理消息插入操作的服务类。
 * 它允许客户端插入和删除消息。
 */
@Transactional
@Service
public class MessageInsertService {
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private SimpleDateFormat MseeagesSimpleDateFormat;

    /**
     * 此方法用于插入消息。
     * 它首先检查消息的内容，如果内容为空或长度超过500，返回错误。
     * 如果检查通过，它会插入消息。
     *
     * @param text 消息的内容。
     * @param id1  发送者的ID。
     * @param id2  接收者的ID。
     * @return 插入结果。如果消息内容为空或长度超过500，返回"stringError"或"lengthError"，如果插入过程中出现异常，返回"error"，否则返回"success"。
     */
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

    /**
     * 此方法用于删除好友。
     * 它会删除两个用户之间的所有消息。
     *
     * @param id1 用户1的ID。
     * @param id2 用户2的ID。
     * @return 删除结果，总是返回"success"。
     */
    public String deleteFriend(Integer id1, Integer id2) {
        messageMapper.deleteMessage(id1, id2);
        return "success";
    }
}