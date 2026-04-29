package com.miniblog.controller;

import com.miniblog.entity.Message;
import com.miniblog.service.MessageService;
import com.miniblog.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/conversations")
    public Result<List<Map<String, Object>>> conversationList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.fail(401, "未登录");
        return messageService.conversationList(userId);
    }

    @GetMapping("/{otherId}")
    public Result<List<Map<String, Object>>> conversation(
            @PathVariable Long otherId,
            @RequestParam(defaultValue = "50") int limit,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.fail(401, "未登录");
        return messageService.conversation(userId, otherId, limit);
    }

    @PostMapping("/{receiverId}")
    public Result<Message> send(
            @PathVariable Long receiverId,
            @RequestBody Map<String, String> body,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.fail(401, "未登录");
        String content = body.get("content");
        return messageService.send(userId, receiverId, content);
    }

    @PutMapping("/{senderId}/read")
    public Result<Void> markRead(@PathVariable Long senderId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.fail(401, "未登录");
        return messageService.markAsRead(userId, senderId);
    }

    @GetMapping("/unread-count")
    public Result<Long> unreadCount(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.ok(0L);
        return messageService.unreadCount(userId);
    }
}
