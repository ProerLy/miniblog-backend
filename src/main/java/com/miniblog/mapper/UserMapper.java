package com.miniblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miniblog.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    User findByUsername(@Param("username") String username);
}
