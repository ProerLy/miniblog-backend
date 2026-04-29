package com.miniblog.controller;

import com.miniblog.service.CommentService;
import com.miniblog.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public Result<Map<String, Object>> list(
            @PathVariable Long articleId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "latest") String sort,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return commentService.list(articleId, page, size, sort, userId);
    }

    @PostMapping
    public Result<Void> add(
            @PathVariable Long articleId,
            @RequestBody Map<String, Object> body,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.fail(401, "未登录");
        String content = (String) body.get("content");
        Object pid = body.get("parentId");
        Long parentId = pid != null ? Long.valueOf(pid.toString()) : 0L;
        return commentService.add(articleId, content, parentId, userId);
    }

    @DeleteMapping("/{commentId}")
    public Result<Void> delete(
            @PathVariable Long articleId,
            @PathVariable Long commentId,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.fail(401, "未登录");
        return commentService.delete(articleId, commentId);
    }

        @PostMapping("/{commentId}/like")
    public Result<Map<String, Object>> toggleLike(
            @PathVariable Long commentId,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.fail(401, "未登录");
        return commentService.toggleLike(commentId, userId);
    }
}
