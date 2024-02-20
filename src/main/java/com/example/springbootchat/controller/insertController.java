package com.example.springbootchat.controller;

import com.example.springbootchat.service.friendRequestService.FriendRequestInsertService;
import com.example.springbootchat.service.messagesService.MessageInsertService;
import com.example.springbootchat.service.userService.UserInsertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequestMapping("/ins")
@CrossOrigin()
public class insertController {
    @Autowired
    private UserInsertService userInsertService;
    @Autowired
    private MessageInsertService messageInsertService;
    @Autowired
    private FriendRequestInsertService friendRequestInsertService;

    @PostMapping("/user")
    public String insertUser(@RequestBody Map<String, String> map) {
        String userName = map.get("userName");
        String password = map.get("password");
        return userInsertService.insertUser(userName, password);
    }

    @PostMapping("/message")
    public String insertMessage(@RequestBody Map<String, String> map) {
        String text = map.get("text");
        Integer id1 = Integer.valueOf(map.get("id1"));
        Integer id2 = Integer.valueOf(map.get("id2"));
        return messageInsertService.insertMessage(text, id1, id2);
    }

    @PostMapping("/friendRequest")
    public String insertFriendRequest(@RequestBody Map<String, String> map) {
            Integer id1 = Integer.valueOf(map.get("id1"));
            Integer id2 = Integer.valueOf(map.get("id2"));
            String text = map.get("text");
        return friendRequestInsertService.insertFriendRequest(id1, id2, text);
    }

    @PostMapping("/FriendRequestAction")
    public String FriendRequestAction(@RequestBody Map<String, String> map) {
        Integer pid = Integer.valueOf(map.get("pid"));
        Integer vid = Integer.valueOf(map.get("vid"));
        String s = map.get("action");
        return friendRequestInsertService.FriendRequestAction(pid, vid, Boolean.parseBoolean(s));
    }

    @PostMapping("/deleteFriend")
//    @CrossOrigin(origins = "https://lt.ximuliunian.top")
    public String deleteFriend(@RequestBody Map<String, String> map) {
        return messageInsertService.deleteFriend(Integer.valueOf(map.get("id1")), Integer.valueOf(map.get("id2")));
    }


}
