package com.example.springbootchat.controller;

import com.example.springbootchat.model.entity.Message;
import com.example.springbootchat.model.entity.friendRequest;
import com.example.springbootchat.model.mapperInterface.FriendRequestMapper;
import com.example.springbootchat.model.mapperInterface.MessageMapper;
import com.example.springbootchat.model.mapperInterface.UserMapper;
import com.example.springbootchat.repository.SessionRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/ins")
@CrossOrigin()
public class insertController {
    @Autowired
    private SessionRepository sessionRepository;

    @PostMapping("/user")
    public String insertUser(@RequestBody Map<String, String> map) {
        try (SqlSession sqlSession = sessionRepository.getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            String userName = map.get("userName");
            String password = map.get("password");
            if (userName == null || password == null) {
                return "stringError";
            }
            if (userName.length() > 20 || password.length() > 20 || userName.length() < 6 || password.length() < 6) {
                return "lengthError";
            }
            if (userMapper.selectUserByName(userName) != null) {
                return "existError";
            }
            try {
                HashMap<Object, Object> map1 = new HashMap<>();
                map1.put("userName", userName);
                map1.put("password", password);
                map1.put("age", null);
                userMapper.insertUser(map1);
                sqlSession.commit();
            } catch (Exception e) {
                return "error";
            }
            return "success";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/message")//
    public String insertMessage(@RequestBody Map<String, String> map) {
        try (SqlSession sqlSession = sessionRepository.getSqlSession()) {
            MessageMapper messageMapper = sqlSession.getMapper(MessageMapper.class);
            String text = map.get("text");
            Integer id1 = Integer.valueOf(map.get("id1"));
            Integer id2 = Integer.valueOf(map.get("id2"));
            if (text == null) {
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                map1.put("datetime", simpleDateFormat.format(new Date()));
                messageMapper.insertMessage(map1);
                sqlSession.commit();
            } catch (Exception e) {
                return "error";
            }
            return "success";
        }

    }

    @PostMapping("/friendRequest")
    public String insertFriendRequest(@RequestBody Map<String, String> map) {
        try (SqlSession sqlSession = sessionRepository.getSqlSession()) {
            FriendRequestMapper friendRequestMapper = sqlSession.getMapper(FriendRequestMapper.class);
            MessageMapper messageMapper = sqlSession.getMapper(MessageMapper.class);
            Integer id1 = Integer.valueOf(map.get("id1"));
            Integer id2 = Integer.valueOf(map.get("id2"));
            List<Message> messages = messageMapper.selectAllMessageByU1IdAndU2Id(id1, id2);
            //如果pid和vid是好友直接返回Exist
            if (messages.size() != 0) {
                return "exist";
            }
            //如果pid已经向vid发送过好友请求直接返回RequestExist
            if (friendRequestMapper.selectFriendRequestByVidAndPid(id2, id1) != null) {
                return "RequestExist";
            }
            String text = map.get("text");
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id1", id1);
            hashMap.put("id2", id2);
            hashMap.put("text", text);
            hashMap.put("time", new java.sql.Date(System.currentTimeMillis()));
            return friendRequestMapper.insertFriendRequest(hashMap).toString();
        } catch (NumberFormatException e) {
            System.out.println("----------------------------------Error----------------------------------");
            System.out.println("无法转换id");
        }
        return "error";
    }

    @PostMapping("/FriendRequestAction")
    public String FriendRequestAction(@RequestBody Map<String, String> map) {
        try (SqlSession sqlSession = sessionRepository.getSqlSession()) {
            FriendRequestMapper friendRequestMapper = sqlSession.getMapper(FriendRequestMapper.class);
            MessageMapper messageMapper = sqlSession.getMapper(MessageMapper.class);
            Integer pid = Integer.valueOf(map.get("pid"));
            Integer vid = Integer.valueOf(map.get("vid"));
            friendRequest friendRequest = friendRequestMapper.selectFriendRequestByVidAndPid(pid, vid);
            if (friendRequest != null) {
                String s = map.get("action");
                if ("true".equals(s)) {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/deleteFriend")
//    @CrossOrigin(origins = "https://lt.ximuliunian.top")
    public String deleteFriend(@RequestBody Map<String, String> map) {
        try (SqlSession sqlSession = sessionRepository.getSqlSession()) {
            MessageMapper mapper = sqlSession.getMapper(MessageMapper.class);
            mapper.deleteMessage(Integer.valueOf(map.get("id1")), Integer.valueOf(map.get("id2")));
            return "success";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
