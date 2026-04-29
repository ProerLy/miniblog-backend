package com.miniblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miniblog.entity.Follow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FollowMapper extends BaseMapper<Follow> {
    Follow findByPair(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
}
