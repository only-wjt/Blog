package com.onlyWjt.controller;

import com.onlyWjt.domain.dto.TagListDto;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.domain.view.CategoryVo;
import com.onlyWjt.domain.view.PageVo;
import com.onlyWjt.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult getAllCategory(){
        List<CategoryVo> categoryVoList = categoryService.getAllCategory();
        return ResponseResult.okResult(categoryVoList);
    }
}
