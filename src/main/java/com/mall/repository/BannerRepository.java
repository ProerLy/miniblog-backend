package com.mall.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.entity.Banner;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BannerRepository extends BaseMapper<Banner> {
}
