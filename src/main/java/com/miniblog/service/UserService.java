package com.miniblog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miniblog.dto.ArticleVo;
import com.miniblog.entity.Follow;
import com.miniblog.entity.User;
import com.miniblog.mapper.ArticleLikeMapper;
import com.miniblog.mapper.ArticleMapper;
import com.miniblog.mapper.CollectMapper;
import com.miniblog.mapper.FollowMapper;
import com.miniblog.mapper.UserMapper;
import com.miniblog.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ArticleLikeMapper articleLikeMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private FollowMapper followMapper;

    public Result<User> getProfile(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) return Result.fail(404, "用户不存在");
        user.setPassword(null);
        return Result.ok(user);
    }

    public Result<Void> updateProfile(Long userId, String nickname, String avatar, String bio) {
        User user = userMapper.selectById(userId);
        if (nickname != null) user.setNickname(nickname);
        if (avatar != null) user.setAvatar(avatar);
        if (bio != null) user.setBio(bio);
        userMapper.updateById(user);
        return Result.ok();
    }

    public Result<java.util.Map<String, Object>> getUserArticles(Long userId, int page, int size) {
        Page<ArticleVo> p = new Page<>(page, size);
        IPage<ArticleVo> result = articleMapper.selectUserArticlesPage(p, userId);
        return Result.ok(java.util.Map.of("records", result.getRecords(), "total", result.getTotal()));
    }

    public Result<java.util.Map<String, Object>> getUserFavorites(Long userId, int page, int size) {
        Page<ArticleVo> p = new Page<>(page, size);
        IPage<ArticleVo> result = articleMapper.selectUserFavoritesPage(p, userId);
        return Result.ok(java.util.Map.of("records", result.getRecords(), "total", result.getTotal()));
    }

    public Result<Void> follow(Long currentUserId, Long targetUserId) {
        Follow exist = followMapper.findByPair(currentUserId, targetUserId);
        if (exist == null) {
            Follow f = new Follow();
            f.setFollowerId(currentUserId);
            f.setFollowingId(targetUserId);
            followMapper.insert(f);
        }
        return Result.ok();
    }

    public Result<Void> unfollow(Long currentUserId, Long targetUserId) {
        LambdaQueryWrapper<Follow> q = new LambdaQueryWrapper<>();
        q.eq(Follow::getFollowerId, currentUserId).eq(Follow::getFollowingId, targetUserId);
        followMapper.delete(q);
        return Result.ok();
    }

    public Result<Object> stats(Long userId) {
        long following = followMapper.selectCount(
            new LambdaQueryWrapper<Follow>().eq(Follow::getFollowerId, userId));
        long followers = followMapper.selectCount(
            new LambdaQueryWrapper<Follow>().eq(Follow::getFollowingId, userId));
        long likes = articleLikeMapper.countByUserId(userId);
        return Result.ok(java.util.Map.of("followers", followers, "following", following, "likes", likes));
    }
}
