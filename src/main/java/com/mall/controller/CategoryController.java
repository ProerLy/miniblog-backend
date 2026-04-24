package com.mall.controller;

import com.mall.entity.Category;
import com.mall.entity.Result;
import com.mall.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@CrossOrigin
public class CategoryController {
    private final GoodsService goodsService;

    /**
     * 分类列表（与 /api/goods/categories 相同路径不同）
     */
    @GetMapping("/list")
    public Result<List<Category>> list() {
        return Result.ok(goodsService.getCategories());
    }
}
