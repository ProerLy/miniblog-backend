package com.miniblog.controller;

import com.miniblog.service.CollectService;
import com.miniblog.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CollectController {

    @Autowired
    private CollectService collectService;

    @GetMapping("/articles/{id}/collect")
    public Result<Boolean> hasCollect(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.ok(false);
        return collectService.hasCollect(id, userId);
    }

    @PostMapping("/articles/{id}/collect")
    public Result<Boolean> toggle(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.fail(401, "未登录");
        return collectService.toggle(id, userId);
    }

    @GetMapping("/collects")
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.fail(401, "未登录");
        return collectService.list(userId, page, size);
    }
}
