package com.miniblog.service;

import com.miniblog.entity.Category;
import com.miniblog.mapper.CategoryMapper;
import com.miniblog.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public Result<List<Category>> list() {
        return Result.ok(categoryMapper.selectList(null));
    }
}
