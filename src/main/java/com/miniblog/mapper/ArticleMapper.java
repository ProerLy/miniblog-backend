package com.miniblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.miniblog.dto.ArticleVo;
import com.miniblog.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    IPage<ArticleVo> selectListPage(IPage<ArticleVo> page,
        @Param("categoryId") Long categoryId,
        @Param("tag") String tag,
        @Param("keyword") String keyword);

    java.util.List<ArticleVo> selectBanner(@Param("limit") int limit);

    java.util.List<ArticleVo> selectRelated(@Param("id") Long id);

    IPage<ArticleVo> selectUserArticlesPage(IPage<ArticleVo> page, @Param("userId") Long userId);

    IPage<ArticleVo> selectUserFavoritesPage(IPage<ArticleVo> page, @Param("userId") Long userId);

    ArticleVo selectDetail(@Param("id") Long id);
}
