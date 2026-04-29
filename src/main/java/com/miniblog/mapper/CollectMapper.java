package com.miniblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miniblog.entity.Collect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CollectMapper extends BaseMapper<Collect> {
    Collect findByUserAndArticle(@Param("userId") Long userId, @Param("articleId") Long articleId);
}
