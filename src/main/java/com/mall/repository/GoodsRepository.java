package com.mall.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsRepository extends BaseMapper<Goods> {
}
