package com.example.springbootchat.service.friendRequestService;

import com.example.springbootchat.model.entity.friendRequest;
import com.example.springbootchat.model.mapperInterface.FriendRequestMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 这是一个处理好友请求查询操作的服务类。
 * 它允许客户端查询好友请求。
 */
@Transactional(readOnly = true)
@Service
public class FriendRequestSelectService {
    @Resource
    private FriendRequestMapper friendRequestMapper;

    /**
     * 此方法用于查询好友请求。
     * 它会返回给定ID的用户的所有好友请求。
     *
     * @param id 用户的ID。
     * @return 一个包含所有好友请求的列表。
     */
    public List<friendRequest> queryFriendRequest(Integer id) {
        return friendRequestMapper.selectFriendRequestByVid(id);
    }

}