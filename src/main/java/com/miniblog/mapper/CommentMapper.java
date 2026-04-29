package com.miniblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miniblog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    List<Map<String, Object>> selectByArticleId(@Param("articleId") Long articleId, @Param("sort") String sort, @Param("userId") Long userId);
}
