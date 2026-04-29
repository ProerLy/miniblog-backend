package com.miniblog.controller;

import com.miniblog.dto.ArticleVo;
import com.miniblog.entity.Article;
import com.miniblog.service.ArticleService;
import com.miniblog.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String keyword) {
        return articleService.list(page, size, categoryId, tag, keyword);
    }

    @GetMapping("/{id}")
    public Result<ArticleVo> detail(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return articleService.detail(id, userId);
    }

    @PostMapping
    public Result<Void> publish(@RequestBody Article article, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.fail(401, "未登录");
        article.setUserId(userId);
        return articleService.publish(article);
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Article article, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.fail(401, "未登录");
        return articleService.update(id, article, userId);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.fail(401, "未登录");
        return articleService.delete(id, userId);
    }

    @GetMapping("/{id}/like")
    public Result<Boolean> getLike(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return articleService.getLikeStatus(id, userId);
    }

    @PostMapping("/{id}/like")
    public Result<Boolean> toggleLike(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.fail(401, "未登录");
        return articleService.toggleLike(id, userId);
    }

    @GetMapping("/related/{id}")
    public Result<List<ArticleVo>> related(@PathVariable Long id) {
        return articleService.related(id);
    }

    @GetMapping("/banner")
    public Result<List<ArticleVo>> banner(@RequestParam(defaultValue = "5") int limit) {
        return articleService.banner(limit);
    }
}
