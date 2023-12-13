package com.example.springbootchat.model.mapperInterface;

import com.example.springbootchat.model.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    User selectUserById(int id);

    User selectUserByName(String name);

    User selectUserByNameAndPassword(@Param("userName") String userName, @Param("password") String password);

    Integer insertUser(Map map);

    List<User> selectFriendById(int id);
}
