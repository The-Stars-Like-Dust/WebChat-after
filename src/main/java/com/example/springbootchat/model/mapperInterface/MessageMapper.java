package com.example.springbootchat.model.mapperInterface;


import com.example.springbootchat.model.entry.Message;

import java.util.List;
import java.util.Map;

public interface MessageMapper {
    List<Message> selectAllMessageByU1IdAndU2Id(Integer id1, Integer id2);

    Integer insertMessage(Map map);

    List<Message> selectAllMessageByU1IdAndU2IdAndTime(Integer id1, Integer id2, String time);

    Integer deleteMessage(Integer id1, Integer id2);
}