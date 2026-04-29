package com.miniblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miniblog.entity.ArticleLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleLikeMapper extends BaseMapper<ArticleLike> {
    ArticleLike findByUserAndArticle(@Param("userId") Long userId, @Param("articleId") Long articleId);
    Long countByUserId(@Param("userId") Long userId);
}
