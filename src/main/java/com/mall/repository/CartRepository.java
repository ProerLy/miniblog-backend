package com.mall.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.entity.Cart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartRepository extends BaseMapper<Cart> {
}
