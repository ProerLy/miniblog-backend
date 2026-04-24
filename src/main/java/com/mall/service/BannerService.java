package com.mall.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.entity.Banner;
import com.mall.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerRepository bannerRepository;

    public List<Banner> getHomeBanners() {
        return bannerRepository.selectList(
            new LambdaQueryWrapper<Banner>()
                .eq(Banner::getType, 1)
                .orderByAsc(Banner::getSort)
        );
    }
}
