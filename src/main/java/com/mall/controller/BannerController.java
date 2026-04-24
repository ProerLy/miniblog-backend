package com.mall.controller;

import com.mall.entity.Banner;
import com.mall.entity.Result;
import com.mall.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/banner")
@RequiredArgsConstructor
@CrossOrigin
public class BannerController {
    private final BannerService bannerService;

    @GetMapping("/home")
    public Result<List<Banner>> getHomeBanners() {
        return Result.ok(bannerService.getHomeBanners());
    }

    /** 前端 /api/banner/list */
    @GetMapping("/list")
    public Result<List<Banner>> getBannerList() {
        return Result.ok(bannerService.getHomeBanners());
    }
}
