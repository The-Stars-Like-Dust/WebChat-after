package com.example.springbootchat.service.userService;

import com.example.springbootchat.model.entity.Message;
import com.example.springbootchat.model.entity.User;
import com.example.springbootchat.model.entity.friendRequest;
import com.example.springbootchat.model.mapperInterface.FriendRequestMapper;
import com.example.springbootchat.model.mapperInterface.MessageMapper;
import com.example.springbootchat.model.mapperInterface.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Transactional(readOnly = true)
@Service
public class UserSelectService {
    private static HashMap<String, String> userMap = new HashMap<>();
    @Resource
    private UserMapper userMapper;
    @Resource
    private FriendRequestMapper friendRequestMapper;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private RedisTemplate redisTemplate;

    public String selectUser(String userName, String password) {
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
        return t;
    }

    public User selectUserObject(String userName, String password) {
        return userMapper.selectUserByNameAndPassword(userName, password);
    }

    public HashMap<String, String> selectUuid(String uuid) {
        String s1 = userMap.get(uuid);
        if (s1 == null) {
            return null;
        }
        userMap.remove(uuid);
        String[] split = s1.split("[.]");
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("userName", split[0]);
        stringStringHashMap.put("id", split[1]);
        return stringStringHashMap;
    }

    public User queryUser(String name, Integer inquirerID) {
        User user = userMapper.selectUserByName(name);
        if (user == null) {
            return null;
        }
        //如果查询的是自己
        if (inquirerID == user.getId()) {
            return null;
        }
        //是否发送过好友请求
        friendRequest friendRequest = friendRequestMapper.selectFriendRequestByVidAndPid(user.getId(), inquirerID);
        if (friendRequest != null) {
            return new User(0, "0", "0");
        }
        List<Message> messages = messageMapper.selectAllMessageByU1IdAndU2Id(inquirerID, user.getId());
        if (messages.size() == 0) {
            return user;
        } else {
            return new User();
        }
    }

    public List<User> selectFriend(Integer id) {
        return userMapper.selectFriendById(id);
    }
}
