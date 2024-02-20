package com.example.springbootchat.model.mapperInterface;

import com.example.springbootchat.model.entity.friendRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface FriendRequestMapper {

    /**
     * 插入好友请求
     *
     * @param map 地图
     * @return {@link Integer}
     */
    Integer insertFriendRequest(Map map);

    List<friendRequest> selectFriendRequestByVid(Integer vid);

    Integer deleteFriendRequestByPidAndVid(Integer pid, Integer vid);

    friendRequest selectFriendRequestByVidAndPid(Integer vid, Integer pid);

}
