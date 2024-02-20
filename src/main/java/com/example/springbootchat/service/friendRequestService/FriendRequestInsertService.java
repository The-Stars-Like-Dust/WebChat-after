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

@Transactional
@Service
public class FriendRequestInsertService {
    @Resource
    private FriendRequestMapper friendRequestMapper;
    @Resource
    private MessageMapper messageMapper;

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
