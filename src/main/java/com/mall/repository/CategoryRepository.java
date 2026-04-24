package com.mall.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryRepository extends BaseMapper<Category> {
}
