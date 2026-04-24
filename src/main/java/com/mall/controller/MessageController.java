package com.mall.controller;

import com.mall.entity.Message;
import com.mall.entity.Result;
import com.mall.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
@CrossOrigin
public class MessageController {
    private final MessageService messageService;

    private Long getUserId() {
        return 1L;
    }

    @GetMapping("/list")
    public Result<List<Message>> getMessageList() {
        return Result.ok(messageService.getMessageList(getUserId()));
    }

    @GetMapping("/unread")
    public Result<List<Message>> getUnreadList() {
        return Result.ok(messageService.getUnreadList(getUserId()));
    }

    @GetMapping("/unread/count")
    public Result<Integer> getUnreadCount() {
        return Result.ok(messageService.getUnreadCount(getUserId()));
    }

    @PutMapping("/read/{id}")
    public Result<Void> markAsRead(@PathVariable Long id) {
        messageService.markAsRead(id);
        return Result.ok();
    }

    @PutMapping("/read/all")
    public Result<Void> markAllAsRead() {
        messageService.markAllAsRead(getUserId());
        return Result.ok();
    }
}
