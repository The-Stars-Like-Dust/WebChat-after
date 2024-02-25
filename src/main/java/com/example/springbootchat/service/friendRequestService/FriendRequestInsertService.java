package com.example.springbootchat.service.friendRequestService;

import com.example.springbootchat.model.entity.Message;
import com.example.springbootchat.model.entity.friendRequest;
import com.example.springbootchat.model.mapperInterface.FriendRequestMapper;
import com.example.springbootchat.model.mapperInterface.MessageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * 这是一个处理好友请求插入操作的服务类。
 * 它允许客户端插入好友请求和处理好友请求。
 */
@Transactional
@Service
public class FriendRequestInsertService {
    @Resource
    private FriendRequestMapper friendRequestMapper;
    @Resource
    private MessageMapper messageMapper;

    /**
     * 此方法用于处理好友请求。
     * 如果同意请求，它会在两个用户之间插入一条消息，然后删除好友请求。
     * 如果不同意请求，它只删除好友请求。
     *
     * @param pid     请求者的ID。
     * @param vid     被请求者的ID。
     * @param consent 是否同意请求。
     * @return 如果好友请求不存在，返回"error"，否则返回"success"。
     */
    public String FriendRequestAction(Integer pid, Integer vid, boolean consent) {
        friendRequest friendRequest = friendRequestMapper.selectFriendRequestByVidAndPid(pid, vid);
        if (friendRequest != null) {
            if (consent) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("pid", pid);
                hashMap.put("vid", vid);
                hashMap.put("message", "你好，我们已经成为好友啦");
                hashMap.put("datetime", new java.sql.Timestamp(System.currentTimeMillis()));
                messageMapper.insertMessage(hashMap);
                hashMap.put("pid", vid);
                hashMap.put("vid", pid);
                messageMapper.insertMessage(hashMap);
            }
            friendRequestMapper.deleteFriendRequestByPidAndVid(vid, pid);
        } else return "error";
        return "success";
    }

    /**
     * 此方法用于插入好友请求。
     * 它首先检查两个用户是否已经是好友，如果是，返回"exist"。
     * 然后检查是否已经发送过好友请求，如果是，返回"RequestExist"。
     * 如果以上检查都通过，它会插入好友请求。
     *
     * @param id1  请求者的ID。
     * @param id2  被请求者的ID。
     * @param text 请求的文本。
     * @return 插入结果。
     */
    public String insertFriendRequest(Integer id1, Integer id2, String text) {
        List<Message> messages = messageMapper.selectAllMessageByU1IdAndU2Id(id1, id2);
        //如果pid和vid是好友直接返回Exist
        if (messages.size() != 0) {
            return "exist";
        }
        //如果pid已经向vid发送过好友请求直接返回RequestExist
        if (friendRequestMapper.selectFriendRequestByVidAndPid(id2, id1) != null) {
            return "RequestExist";
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id1", id1);
        hashMap.put("id2", id2);
        hashMap.put("text", text);
        hashMap.put("time", new java.sql.Date(System.currentTimeMillis()));
        return friendRequestMapper.insertFriendRequest(hashMap).toString();
    }
}