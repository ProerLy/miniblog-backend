package com.mall.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.entity.*;
import com.mall.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsService {
    private final GoodsRepository goodsRepository;
    private final CategoryRepository categoryRepository;

    public List<Goods> getHotGoods(int limit) {
        return goodsRepository.selectList(
            new LambdaQueryWrapper<Goods>()
                .eq(Goods::getIsHot, 1)
                .orderByDesc(Goods::getSales)
                .last("LIMIT " + limit)
        );
    }

    public List<Goods> getNewGoods(int limit) {
        return goodsRepository.selectList(
            new LambdaQueryWrapper<Goods>()
                .eq(Goods::getIsNew, 1)
                .orderByDesc(Goods::getCreateTime)
                .last("LIMIT " + limit)
        );
    }

    public List<Goods> getRecommendGoods(int limit) {
        return goodsRepository.selectList(
            new LambdaQueryWrapper<Goods>()
                .eq(Goods::getIsRec, 1)
                .orderByDesc(Goods::getSales)
                .last("LIMIT " + limit)
        );
    }

    public PageResult<Goods> search(String keyword, Long categoryId, int page, int pageSize) {
        LambdaQueryWrapper<Goods> qw = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            qw.like(Goods::getName, keyword);
        }
        if (categoryId != null) {
            qw.eq(Goods::getCategoryId, categoryId);
        }
        qw.orderByDesc(Goods::getSales);
        Page<Goods> p = goodsRepository.selectPage(new Page<>(page, pageSize), qw);
        return PageResult.of(p.getTotal(), p.getRecords());
    }

    public Goods getDetail(Long id) {
        return goodsRepository.selectById(id);
    }

    public List<Category> getCategories() {
        return categoryRepository.selectList(
            new LambdaQueryWrapper<Category>().orderByAsc(Category::getSort)
        );
    }

    public PageResult<Goods> getGoodsByCategory(Long categoryId, int page, int pageSize) {
        LambdaQueryWrapper<Goods> qw = new LambdaQueryWrapper<Goods>().eq(Goods::getCategoryId, categoryId);
        qw.orderByDesc(Goods::getSales);
        Page<Goods> p = goodsRepository.selectPage(new Page<>(page, pageSize), qw);
        return PageResult.of(p.getTotal(), p.getRecords());
    }
}
