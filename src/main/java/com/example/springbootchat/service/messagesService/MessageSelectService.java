package com.example.springbootchat.service.messagesService;

import com.example.springbootchat.model.entity.Message;
import com.example.springbootchat.model.mapperInterface.MessageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class MessageSelectService {
    @Resource
    private MessageMapper messageMapper;

    public List<Message> selectMessage(Integer id1, Integer id2) {
        System.out.println(messageMapper);
        return messageMapper.selectAllMessageByU1IdAndU2Id(id1, id2);
    }

    public List<Message> selectMessageByTime(Integer id1, Integer id2, String time) {
        return messageMapper.selectAllMessageByU1IdAndU2IdAndTime(id1, id2, time);
    }


}
