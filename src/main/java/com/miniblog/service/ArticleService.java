package com.miniblog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miniblog.dto.ArticleVo;
import com.miniblog.entity.Article;
import com.miniblog.entity.ArticleLike;
import com.miniblog.mapper.ArticleLikeMapper;
import com.miniblog.mapper.ArticleMapper;
import com.miniblog.mapper.CollectMapper;
import com.miniblog.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private ArticleLikeMapper articleLikeMapper;

    public Result<java.util.Map<String, Object>> list(int page, int size, Long categoryId, String tag, String keyword) {
        Page<ArticleVo> p = new Page<>(page, size);
        IPage<ArticleVo> result = articleMapper.selectListPage(p, categoryId, tag, keyword);
        for (ArticleVo vo : result.getRecords()) {
            vo.setCreateTime(format(vo.getCreateTime()));
        }
        java.util.Map<String, Object> map = new java.util.LinkedHashMap<>();
        map.put("records", result.getRecords());
        map.put("total", result.getTotal());
        return Result.ok(map);
    }

    public Result<ArticleVo> detail(Long id, Long userId) {
        ArticleVo vo = articleMapper.selectDetail(id);
        if (vo == null) return Result.fail(404, "文章不存在");
        // 浏览量+1（只更新当前文章）
        articleMapper.update(null,
            new LambdaUpdateWrapper<Article>()
                .eq(Article::getId, id)
                .setSql("view_count = view_count + 1"));
        // 检查当前用户是否已点赞/收藏
        if (userId != null) {
            vo.setIsLiked(articleLikeMapper.findByUserAndArticle(userId, id) != null);
            vo.setIsFavorited(collectMapper.findByUserAndArticle(userId, id) != null);
        } else {
            vo.setIsLiked(false);
            vo.setIsFavorited(false);
        }
        vo.setCreateTime(format(vo.getCreateTime()));
        return Result.ok(vo);
    }

    @Transactional
    public Result<Void> publish(Article article) {
        articleMapper.insert(article);
        return Result.ok();
    }

    @Transactional
    public Result<Void> update(Long id, Article article, Long currentUserId) {
        Article exist = articleMapper.selectById(id);
        if (exist == null) return Result.fail(404, "文章不存在");
        if (!exist.getUserId().equals(currentUserId)) return Result.fail(403, "无权修改他人文章");
        article.setId(id);
        articleMapper.updateById(article);
        return Result.ok();
    }

    @Transactional
    public Result<Void> delete(Long id, Long currentUserId) {
        Article exist = articleMapper.selectById(id);
        if (exist == null) return Result.fail(404, "文章不存在");
        if (!exist.getUserId().equals(currentUserId)) return Result.fail(403, "无权删除他人文章");
        articleMapper.deleteById(id);
        return Result.ok();
    }

    public Result<Boolean> getLikeStatus(Long articleId, Long userId) {
        if (userId == null) return Result.ok(false);
        return Result.ok(articleLikeMapper.findByUserAndArticle(userId, articleId) != null);
    }

    @Transactional
    public Result<Boolean> toggleLike(Long articleId, Long userId) {
        ArticleLike exist = articleLikeMapper.findByUserAndArticle(userId, articleId);
        if (exist != null) {
            // 取消点赞
            articleLikeMapper.deleteById(exist.getId());
            articleMapper.update(null,
                new LambdaUpdateWrapper<Article>()
                    .eq(Article::getId, articleId)
                    .setSql("like_count = GREATEST(like_count - 1, 0)"));
            return Result.ok(false);
        }
        // 点赞
        ArticleLike like = new ArticleLike();
        like.setUserId(userId);
        like.setArticleId(articleId);
        articleLikeMapper.insert(like);
        articleMapper.update(null,
            new LambdaUpdateWrapper<Article>()
                .eq(Article::getId, articleId)
                .setSql("like_count = like_count + 1"));
        return Result.ok(true);
    }

    public Result<List<ArticleVo>> related(Long id) {
        List<ArticleVo> list = articleMapper.selectRelated(id);
        for (ArticleVo vo : list) {
            vo.setCreateTime(format(vo.getCreateTime()));
        }
        return Result.ok(list);
    }

    public Result<List<ArticleVo>> banner(int limit) {
        List<ArticleVo> list = articleMapper.selectBanner(limit);
        for (ArticleVo vo : list) {
            vo.setCreateTime(format(vo.getCreateTime()));
        }
        return Result.ok(list);
    }

    private String format(Object time) {
        if (time == null) return "";
        return time.toString().substring(0, Math.min(time.toString().length(), 19));
    }
}
