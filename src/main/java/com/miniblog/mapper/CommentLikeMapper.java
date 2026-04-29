package com.miniblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miniblog.entity.CommentLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentLikeMapper extends BaseMapper<CommentLike> {
    CommentLike selectByUserIdAndCommentId(@Param("userId") Long userId, @Param("commentId") Long commentId);
    int countByCommentId(@Param("commentId") Long commentId);
    int deleteByUserIdAndCommentId(@Param("userId") Long userId, @Param("commentId") Long commentId);
}
