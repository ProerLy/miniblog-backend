package com.miniblog.controller;

import com.miniblog.dto.ArticleVo;
import com.miniblog.entity.User;
import com.miniblog.service.UserService;
import com.miniblog.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public Result<User> profile(@PathVariable Long id) {
        return userService.getProfile(id);
    }

    @PutMapping("/profile")
    public Result<Void> updateProfile(
            @RequestBody Map<String, String> body,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.fail(401, "未登录");
        String nickname = body.get("nickname");
        String avatar = body.get("avatar");
        String bio = body.get("bio");
        return userService.updateProfile(userId, nickname, avatar, bio);
    }

    @GetMapping("/{id}/articles")
    public Result<Map<String, Object>> articles(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.getUserArticles(id, page, size);
    }

    @GetMapping("/{id}/favorites")
    public Result<Map<String, Object>> favorites(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.getUserFavorites(id, page, size);
    }

    @GetMapping("/{id}/stats")
    public Result<Object> stats(@PathVariable Long id) {
        return userService.stats(id);
    }

    @GetMapping("/stats")
    public Result<Object> myStats(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.fail(401, "未登录");
        return userService.stats(userId);
    }
}
