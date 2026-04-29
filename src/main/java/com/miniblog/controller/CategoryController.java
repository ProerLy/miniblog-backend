package com.miniblog.controller;

import com.miniblog.entity.Category;
import com.miniblog.service.CategoryService;
import com.miniblog.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Result<List<Category>> list() {
        return categoryService.list();
    }
}
