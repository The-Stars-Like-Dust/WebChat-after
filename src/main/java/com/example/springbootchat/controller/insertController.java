package com.example.springbootchat.controller;

import com.example.springbootchat.exception.PermissionIsAbnormal;
import com.example.springbootchat.model.entity.Phonesms;
import com.example.springbootchat.model.entity.User;
import com.example.springbootchat.service.friendRequestService.FriendRequestInsertService;
import com.example.springbootchat.service.messagesService.MessageInsertService;
import com.example.springbootchat.service.smsService.SMSService;
import com.example.springbootchat.service.userService.UserInsertService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 这是一个处理插入操作的控制器类。
 * 它允许客户端插入用户、消息、好友请求等。
 */
@RestController()
@RequestMapping("/ins")
@CrossOrigin(origins = {"https://lt.ximuliunian.top", "http://192.168.138.1:5500"}, allowCredentials = "true")
public class insertController {
    @Resource
    private UserInsertService userInsertService;
    @Resource
    private MessageInsertService messageInsertService;
    @Resource
    private FriendRequestInsertService friendRequestInsertService;

    @Resource
    private SMSService smsService;

    /**
     * 此方法用于注册用户。
     *
     * @param map 包含用户名、密码、手机号和验证码的映射。
     * @return 插入结果。
     */
    @PostMapping("/user")
    public String insertUser(@RequestBody Map<String, String> map) {
        String userName = map.get("userName");
        String password = map.get("password");
        String phone = map.get("phone");
        String code = map.get("code");
        if (userName == null || password == null) {
            return "stringError";
        }
        if (userName.length() > 20 || password.length() > 20 || userName.length() < 6 || password.length() < 6) {
            return "lengthError";
        }
        Phonesms phonesms = smsService.selectByPrimaryKey(phone);
        if (phonesms.getUid() != null) {
            return "-2"; //手机号已被注册
        }
        if (smsService.checkCode(phone, code)) {
            return userInsertService.insertUser(userName, password, phone);
        }
        return "-1";
    }

    /**
     * 此方法用于插入消息。
     *
     * @param map     包含消息文本、发送者ID和接收者ID的映射。
     * @param session 当前的HTTP会话。
     * @return 插入结果。
     */
    @PostMapping("/message")
    public String insertMessage(@RequestBody Map<String, String> map, HttpSession session) {
        String text = map.get("text");
        int id1 = Integer.valueOf(map.get("id1"));
        int id2 = Integer.valueOf(map.get("id2"));
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if (user.getId() != id1) {
                throw new PermissionIsAbnormal();
            }
            return messageInsertService.insertMessage(text, id1, id2);
        }
        throw new PermissionIsAbnormal();
    }

    /**
     * 此方法用于插入好友请求。
     *
     * @param map     包含发送者ID、接收者ID和请求文本的映射。
     * @param session 当前的HTTP会话。
     * @return 插入结果。
     */
    @PostMapping("/friendRequest")
    public String insertFriendRequest(@RequestBody Map<String, String> map, HttpSession session) {
        int id1 = Integer.valueOf(map.get("id1"));
        int id2 = Integer.valueOf(map.get("id2"));
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if (user.getId() != id1) {
                throw new PermissionIsAbnormal();
            }
            String text = map.get("text");
            return friendRequestInsertService.insertFriendRequest(id1, id2, text);
        }
        throw new PermissionIsAbnormal();
    }

    /**
     * 此方法用于处理好友请求。
     *
     * @param map     包含请求者ID、被请求者ID和操作的映射。
     * @param session 当前的HTTP会话。
     * @return 处理结果。
     */
    @PostMapping("/FriendRequestAction")
    public String FriendRequestAction(@RequestBody Map<String, String> map, HttpSession session) {
        int pid = Integer.valueOf(map.get("pid"));
        int vid = Integer.valueOf(map.get("vid"));
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if (user.getId() != pid) {
                throw new PermissionIsAbnormal();
            }
            String s = map.get("action");
            return friendRequestInsertService.FriendRequestAction(pid, vid, Boolean.parseBoolean(s));
        }
        throw new PermissionIsAbnormal();
    }

    /**
     * 此方法用于删除好友。
     *
     * @param map     包含两个用户ID的映射。
     * @param session 当前的HTTP会话。
     * @return 删除结果。
     */
    @PostMapping("/deleteFriend")
    public String deleteFriend(@RequestBody Map<String, String> map, HttpSession session) {
        int id1 = Integer.parseInt(map.get("id1"));
        int id2 = Integer.parseInt(map.get("id2"));
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if (id1 != user.getId() && id2 != user.getId()) {
                throw new PermissionIsAbnormal();
            }
            return messageInsertService.deleteFriend(id1, id2);
        }
        throw new PermissionIsAbnormal();
    }
}