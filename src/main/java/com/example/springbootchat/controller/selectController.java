package com.example.springbootchat.controller;

import com.example.springbootchat.model.entity.Message;
import com.example.springbootchat.model.entity.User;
import com.example.springbootchat.model.entity.friendRequest;
import com.example.springbootchat.service.friendRequestService.FriendRequestSelectService;
import com.example.springbootchat.service.messagesService.MessageSelectService;
import com.example.springbootchat.service.userService.UserSelectService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/sel")
@CrossOrigin()
public class selectController {
    private static HashMap<String, String> userMap = new HashMap<>();
    @Autowired
    private UserSelectService userSelectService;
    @Resource
    private MessageSelectService messageSelectService;
    @Resource
    private FriendRequestSelectService friendRequestSelectService;
    @Resource
    RedisTemplate<String, String> redisTemplate;

    public selectController() {
    }

    @PostMapping("/users")
    public String selectUser(@RequestBody Map<String, String> map) {
        String userName = map.get("userName");
        String password = map.get("password");
        return userSelectService.selectUser(userName, password);
    }


    @PostMapping("/messages")
    public List<Message> selectMessage(@RequestBody Map<String, String> map) {
        return messageSelectService.selectMessage(Integer.valueOf(map.get("id1")), Integer.valueOf(map.get("id2")));
    }


    @PostMapping("/messages/time")
    public List<Message> selectMessageByTime(@RequestBody Map<String, String> map) {
        return messageSelectService.selectMessageByTime(Integer.valueOf(map.get("id1")), Integer.valueOf(map.get("id2")), map.get("time"));
    }

    @PostMapping("/uuid")
    public HashMap<String, String> selectUuid(@RequestBody Map<String, String> map) {
        String uuid = map.get("uuid");
        return userSelectService.selectUuid(uuid);
    }

    @PostMapping("/friends")
    public List<User> selectFriend(@RequestBody Map<String, String> map) {
        int id = Integer.parseInt(map.get("id"));
        return userSelectService.selectFriend(id);

    }


    @PostMapping("/queryUser")
    public User queryUser(@RequestBody Map<String, String> map) {
        String name = map.get("name");
        Integer inquirerID = Integer.valueOf(map.get("inquirerID"));
        return userSelectService.queryUser(name, inquirerID);
    }


    @PostMapping("/queryFriendRequest")
    public List<friendRequest> queryFriendRequest(@RequestBody Map<String, String> map) {
        int id = Integer.parseInt(map.get("id"));
        return friendRequestSelectService.queryFriendRequest(id);

    }


}
