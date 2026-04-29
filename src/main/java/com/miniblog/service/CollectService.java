package com.miniblog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miniblog.dto.ArticleVo;
import com.miniblog.entity.Collect;
import com.miniblog.mapper.ArticleMapper;
import com.miniblog.mapper.CollectMapper;
import com.miniblog.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

@Service
public class CollectService {

    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private ArticleMapper articleMapper;

    public Result<Boolean> hasCollect(Long articleId, Long userId) {
        return Result.ok(collectMapper.findByUserAndArticle(userId, articleId) != null);
    }

    @Transactional
    public Result<Boolean> toggle(Long articleId, Long userId) {
        Collect exist = collectMapper.findByUserAndArticle(userId, articleId);
        if (exist != null) {
            collectMapper.deleteById(exist.getId());
            return Result.ok(false);
        }
        Collect c = new Collect();
        c.setUserId(userId);
        c.setArticleId(articleId);
        collectMapper.insert(c);
        return Result.ok(true);
    }

    public Result<Map<String, Object>> list(Long userId, int page, int size) {
        LambdaQueryWrapper<Collect> q = new LambdaQueryWrapper<>();
        q.eq(Collect::getUserId, userId);
        Page<Collect> p = new Page<>(page, size);
        IPage<Collect> result = collectMapper.selectPage(p, q);
        // 根据收藏记录查出对应文章
        java.util.List<Long> articleIds = result.getRecords().stream()
            .map(Collect::getArticleId).toList();
        java.util.List<ArticleVo> records = new java.util.ArrayList<>();
        if (!articleIds.isEmpty()) {
            for (Long aid : articleIds) {
                ArticleVo vo = articleMapper.selectDetail(aid);
                if (vo != null) {
                    vo.setIsFavorited(true);
                    records.add(vo);
                }
            }
        }
        java.util.Map<String, Object> map = new java.util.LinkedHashMap<>();
        map.put("records", records);
        map.put("total", result.getTotal());
        return Result.ok(map);
    }
}
