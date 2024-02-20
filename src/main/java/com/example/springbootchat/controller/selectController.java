package com.example.springbootchat.controller;

import com.example.springbootchat.model.entity.Message;
import com.example.springbootchat.model.entity.User;
import com.example.springbootchat.model.entity.friendRequest;
import com.example.springbootchat.model.mapperInterface.FriendRequestMapper;
import com.example.springbootchat.model.mapperInterface.MessageMapper;
import com.example.springbootchat.model.mapperInterface.UserMapper;
import com.example.springbootchat.repository.SessionRepository;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController()
@RequestMapping("/sel")
@CrossOrigin()
public class selectController {
    static HashMap<String, String> userMap = new HashMap<>();
    @Autowired
    private SessionRepository sessionRepository;
    @Resource
    RedisTemplate<String, String> redisTemplate;

    public selectController() {
    }

    @PostMapping("/users")
    public String selectUser(@RequestBody Map<String, String> map) {
        try (SqlSession sqlSession = sessionRepository.getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            String userName = map.get("userName");
            String password = map.get("password");
            User user = userMapper.selectUserByNameAndPassword(userName, password);
            if (user == null) {
                return "passwordError";
            }
            //查找是否已经登录
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String key = "user_" + user.getId();
            String value = valueOperations.get(key);
            if (value != null) {
                return null;
            } else {
                valueOperations.set(key, "1", 10, TimeUnit.SECONDS);
            }
            int i = new Random().nextInt();
            String t = Integer.toString(i);
            userMap.put(t, user.getUserName() + "." + user.getId());
            sqlSession.commit();
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/messages")
    public List<Message> selectMessage(@RequestBody Map<String, String> map) {
        try (SqlSession sqlSession = sessionRepository.getSqlSession()) {
            MessageMapper messageMapper = sqlSession.getMapper(MessageMapper.class);
            return messageMapper.selectAllMessageByU1IdAndU2Id(Integer.valueOf(map.get("id1")), Integer.valueOf(map.get("id2")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/messages/time")
    public List<Message> selectMessageByTime(@RequestBody Map<String, String> map) {
        try (SqlSession sqlSession = sessionRepository.getSqlSession()) {
            MessageMapper messageMapper = sqlSession.getMapper(MessageMapper.class);
            return messageMapper.selectAllMessageByU1IdAndU2IdAndTime(Integer.valueOf(map.get("id1")), Integer.valueOf(map.get("id2")), map.get("time"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/uuid")
    public HashMap<String, String> selectUuid(@RequestBody Map<String, String> map) {
        try {
            String s = map.get("uuid");
            String s1 = userMap.get(s);
            if (s1 == null) {
                return null;
            }
            userMap.remove(s);
            String[] split = s1.split("[.]");
            HashMap<String, String> stringStringHashMap = new HashMap<>();
            stringStringHashMap.put("userName", split[0]);
            stringStringHashMap.put("id", split[1]);
            return stringStringHashMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @PostMapping("/friends")
    public List<User> selectFriend(@RequestBody Map<String, String> map) {
        try (SqlSession sqlSession = sessionRepository.getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            int id = Integer.parseInt(map.get("id"));
            return userMapper.selectFriendById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/queryUser")
    public User queryUser(@RequestBody Map<String, String> map) {
        try (SqlSession sqlSession = sessionRepository.getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            MessageMapper messageMapper = sqlSession.getMapper(MessageMapper.class);
            FriendRequestMapper friendRequestMapper = sqlSession.getMapper(FriendRequestMapper.class);
            String s = map.get("name");
            Integer id = Integer.valueOf(map.get("inquirerID"));
            User user = userMapper.selectUserByName(s);
            if (user == null) {
                return null;
            }
            //如果查询的是自己
            if (id == user.getId()) {
                return null;
            }
            //是否发送过好友请求
            friendRequest friendRequest = friendRequestMapper.selectFriendRequestByVidAndPid(user.getId(), id);
            if (friendRequest != null) {
                return new User(0, "0", "0");
            }
            List<Message> messages = messageMapper.selectAllMessageByU1IdAndU2Id(id, user.getId());
            if (messages.size() == 0) {
                return user;
            } else {
                return new User();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/queryFriendRequest")
    public List<friendRequest> queryFriendRequest(@RequestBody Map<String, String> map) {
        try (SqlSession sqlSession = sessionRepository.getSqlSession()) {
            FriendRequestMapper friendRequestMapper = sqlSession.getMapper(FriendRequestMapper.class);
            int id = Integer.parseInt(map.get("id"));
            List<friendRequest> friendRequests = friendRequestMapper.selectFriendRequestByVid(id);
            return friendRequests;
        }

    }


}
