package com.miniblog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miniblog.entity.Follow;
import com.miniblog.entity.User;
import com.miniblog.mapper.FollowMapper;
import com.miniblog.mapper.UserMapper;
import com.miniblog.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class FollowService {

    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public Result<Boolean> follow(Long followerId, Long followingId) {
        Follow exist = followMapper.findByPair(followerId, followingId);
        if (exist == null) {
            // 未关注 → 关注
            Follow f = new Follow();
            f.setFollowerId(followerId);
            f.setFollowingId(followingId);
            followMapper.insert(f);
            return Result.ok(true);
        } else {
            // 已关注 → 取消关注
            followMapper.deleteById(exist.getId());
            return Result.ok(false);
        }
    }

    @Transactional
    public Result<Void> unfollow(Long followerId, Long followingId) {
        // 先确认记录存在
        Follow exist = followMapper.findByPair(followerId, followingId);
        if (exist == null) {
            return Result.fail(404, "未关注该用户");
        }
        // 执行删除并检查影响行数
        LambdaQueryWrapper<Follow> q = new LambdaQueryWrapper<>();
        q.eq(Follow::getFollowerId, followerId).eq(Follow::getFollowingId, followingId);
        int rows = followMapper.delete(q);
        System.out.println("[unfollow] followerId=" + followerId + ", followingId=" + followingId + ", deletedRows=" + rows);
        if (rows == 0) {
            return Result.fail(500, "删除失败，未找到记录");
        }
        return Result.ok();
    }

    public Result<Boolean> isFollowing(Long followerId, Long followingId) {
        return Result.ok(followMapper.findByPair(followerId, followingId) != null);
    }

    public Result<java.util.Map<String, Object>> getFollowers(Long userId, int page, int size) {
        com.baomidou.mybatisplus.core.metadata.IPage<Follow> p = new Page<>(page, size);
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Follow> q = new LambdaQueryWrapper<>();
        q.eq(Follow::getFollowingId, userId);
        com.baomidou.mybatisplus.core.metadata.IPage<Follow> result = followMapper.selectPage(p, q);
        List<Long> ids = result.getRecords().stream().map(Follow::getFollowerId).toList();
        List<User> users = ids.isEmpty() ? java.util.Collections.emptyList() : userMapper.selectBatchIds(ids);
        users.forEach(u -> u.setPassword(null));
        return Result.ok(java.util.Map.of("records", users, "total", result.getTotal()));
    }

    public Result<java.util.Map<String, Object>> getFollowing(Long userId, int page, int size) {
        com.baomidou.mybatisplus.core.metadata.IPage<Follow> p = new Page<>(page, size);
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Follow> q = new LambdaQueryWrapper<>();
        q.eq(Follow::getFollowerId, userId);
        com.baomidou.mybatisplus.core.metadata.IPage<Follow> result = followMapper.selectPage(p, q);
        List<Long> ids = result.getRecords().stream().map(Follow::getFollowingId).toList();
        List<User> users = ids.isEmpty() ? java.util.Collections.emptyList() : userMapper.selectBatchIds(ids);
        users.forEach(u -> u.setPassword(null));
        return Result.ok(java.util.Map.of("records", users, "total", result.getTotal()));
    }
}
