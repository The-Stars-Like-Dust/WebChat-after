package com.example.springbootchat.service.messagesService;

import com.example.springbootchat.model.entity.Message;
import com.example.springbootchat.model.mapperInterface.MessageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 这是一个处理消息查询操作的服务类。
 * 它允许客户端根据用户ID和时间选择消息。
 */
@Transactional(readOnly = true)
@Service
public class MessageSelectService {
    @Resource
    private MessageMapper messageMapper;

    /**
     * 此方法用于选择两个用户之间的消息。
     * 它返回由给定ID标识的两个用户之间的所有消息。
     *
     * @param id1 第一个用户的ID。
     * @param id2 第二个用户的ID。
     * @return 两个用户之间的所有消息列表。
     */
    public List<Message> selectMessage(Integer id1, Integer id2) {
        return messageMapper.selectAllMessageByU1IdAndU2Id(id1, id2);
    }

    /**
     * 此方法用于在特定时间内选择两个用户之间的消息。
     * 它返回由给定ID和时间标识的两个用户之间的所有消息。
     *
     * @param id1  第一个用户的ID。
     * @param id2  第二个用户的ID。
     * @param time 选择消息的特定时间。
     * @return 在特定时间内的两个用户之间的所有消息列表。
     */
    public List<Message> selectMessageByTime(Integer id1, Integer id2, String time) {
        return messageMapper.selectAllMessageByU1IdAndU2IdAndTime(id1, id2, time);
    }
}