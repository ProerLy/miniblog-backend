package com.mall.controller;

import com.mall.entity.Goods;
import com.mall.entity.PageResult;
import com.mall.entity.Result;
import com.mall.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/goods")
@RequiredArgsConstructor
@CrossOrigin
public class GoodsController {
    private final GoodsService goodsService;

    @GetMapping("/hot")
    public Result<List<Goods>> getHotGoods() {
        return Result.ok(goodsService.getHotGoods(10));
    }

    @GetMapping("/new")
    public Result<List<Goods>> getNewGoods() {
        return Result.ok(goodsService.getNewGoods(10));
    }

    @GetMapping("/recommend")
    public Result<List<Goods>> getRecommendGoods() {
        return Result.ok(goodsService.getRecommendGoods(10));
    }

    @GetMapping("/search")
    public Result<PageResult<Goods>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        return Result.ok(goodsService.search(keyword, categoryId, page, pageSize));
    }

    @GetMapping("/detail/{id}")
    public Result<Goods> getDetail(@PathVariable Long id) {
        return Result.ok(goodsService.getDetail(id));
    }

    /** RESTful风格商品详情 /api/goods/{id} */
    @GetMapping("/{id}")
    public Result<Goods> getGoodsById(@PathVariable Long id) {
        return Result.ok(goodsService.getDetail(id));
    }

    @GetMapping("/category/{categoryId}")
    public Result<PageResult<Goods>> getByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        return Result.ok(goodsService.getGoodsByCategory(categoryId, page, pageSize));
    }

    @GetMapping("/categories")
    public Result<?> getCategories() {
        return Result.ok(goodsService.getCategories());
    }

    /**
     * 限时秒杀商品（取热卖前5条模拟秒杀）
     */
    @GetMapping("/seckill")
    public Result<List<Goods>> getSeckillGoods() {
        return Result.ok(goodsService.getHotGoods(5));
    }
}
