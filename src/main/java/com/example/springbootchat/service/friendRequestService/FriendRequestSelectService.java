package com.example.springbootchat.service.friendRequestService;

import com.example.springbootchat.model.entity.friendRequest;
import com.example.springbootchat.model.mapperInterface.FriendRequestMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class FriendRequestSelectService {
    @Resource
    private FriendRequestMapper friendRequestMapper;

    public List<friendRequest> queryFriendRequest(Integer id) {
        return friendRequestMapper.selectFriendRequestByVid(id);
    }

}
