package com.miniblog.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.miniblog.entity.Article;
import com.miniblog.entity.Comment;
import com.miniblog.entity.CommentLike;
import com.miniblog.mapper.ArticleMapper;
import com.miniblog.mapper.CommentLikeMapper;
import com.miniblog.mapper.CommentMapper;
import com.miniblog.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentLikeMapper commentLikeMapper;
    @Autowired
    private ArticleMapper articleMapper;

    public Result<Map<String, Object>> list(Long articleId, int page, int size, String sort, Long userId) {
        List<Map<String, Object>> all = commentMapper.selectByArticleId(articleId, sort, userId);
        Map<Long, List<Map<String, Object>>> tree = new LinkedHashMap<>();
        List<Map<String, Object>> roots = new ArrayList<>();
        for (Map<String, Object> c : all) {
            Object pid = c.get("parentId");
            if (pid == null || "0".equals(String.valueOf(pid)) || pid.equals(0L)) {
                roots.add(c);
            } else {
                Long parentId = Long.valueOf(String.valueOf(pid));
                tree.computeIfAbsent(parentId, k -> new ArrayList<>()).add(c);
            }
        }

        int total = roots.size();

        // 对根评论分页
        int fromIndex = Math.max(0, (page - 1) * size);
        int toIndex = Math.min(fromIndex + size, total);
        List<Map<String, Object>> pagedRoots = new ArrayList<>();
        if (fromIndex < total) {
            for (int i = fromIndex; i < toIndex; i++) {
                Map<String, Object> r = roots.get(i);
                Long rid = Long.valueOf(String.valueOf(r.get("id")));
                List<Map<String, Object>> reps = tree.get(rid);
                if (reps != null) {
                    r.put("replies", reps);
                }
                pagedRoots.add(r);
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("list", pagedRoots);
        result.put("total", total);
        return Result.ok(result);
    }

    @Transactional
    public Result<Void> add(Long articleId, String content, Long parentId, Long userId) {
        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setParentId(parentId);
        commentMapper.insert(comment);
        articleMapper.update(null,
            new LambdaUpdateWrapper<Article>()
                .setSql("comment_count = comment_count + 1")
                .eq(Article::getId, articleId));
        return Result.ok();
    }

    @Transactional
    public Result<Void> delete(Long articleId, Long commentId) {
        commentMapper.deleteById(commentId);
        articleMapper.update(null,
            new LambdaUpdateWrapper<Article>()
                .setSql("comment_count = GREATEST(comment_count - 1, 0)")
                .eq(Article::getId, articleId));
        return Result.ok();
    }

    @Transactional
    public Result<Map<String, Object>> toggleLike(Long commentId, Long userId) {
        CommentLike exist = commentLikeMapper.selectByUserIdAndCommentId(userId, commentId);
        boolean liked;
        if (exist != null) {
            // 取消点赞
            commentLikeMapper.deleteByUserIdAndCommentId(userId, commentId);
            commentMapper.update(null,
                new LambdaUpdateWrapper<Comment>()
                    .setSql("like_count = GREATEST(like_count - 1, 0)")
                    .eq(Comment::getId, commentId));
            liked = false;
        } else {
            // 点赞
            CommentLike cl = new CommentLike();
            cl.setUserId(userId);
            cl.setCommentId(commentId);
            cl.setCreatedAt(new Date());
            commentLikeMapper.insert(cl);
            commentMapper.update(null,
                new LambdaUpdateWrapper<Comment>()
                    .setSql("like_count = like_count + 1")
                    .eq(Comment::getId, commentId));
            liked = true;
        }
        int likeCount = commentLikeMapper.countByCommentId(commentId);
        Map<String, Object> data = new HashMap<>();
        data.put("liked", liked);
        data.put("likeCount", likeCount);
        return Result.ok(data);
    }
}
