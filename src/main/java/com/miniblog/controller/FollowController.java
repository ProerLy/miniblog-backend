package com.miniblog.controller;

import com.miniblog.entity.User;
import com.miniblog.service.FollowService;
import com.miniblog.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users/{userId}")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/follow")
    public Result<Boolean> follow(@PathVariable Long userId, HttpServletRequest request) {
        Long currentUserId = (Long) request.getAttribute("userId");
        if (currentUserId == null) return Result.fail(401, "未登录");
        return followService.follow(currentUserId, userId);
    }

    @DeleteMapping("/follow")
    public Result<Void> unfollow(@PathVariable Long userId, HttpServletRequest request) {
        Long currentUserId = (Long) request.getAttribute("userId");
        if (currentUserId == null) return Result.fail(401, "未登录");
        return followService.unfollow(currentUserId, userId);
    }

    @GetMapping("/is-following")
    public Result<Boolean> isFollowing(@PathVariable Long userId, HttpServletRequest request) {
        Long currentUserId = (Long) request.getAttribute("userId");
        if (currentUserId == null) return Result.ok(false);
        return followService.isFollowing(currentUserId, userId);
    }

    @GetMapping("/followers")
    public Result<Map<String, Object>> followers(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return followService.getFollowers(userId, page, size);
    }

    @GetMapping("/following")
    public Result<Map<String, Object>> following(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return followService.getFollowing(userId, page, size);
    }
}
