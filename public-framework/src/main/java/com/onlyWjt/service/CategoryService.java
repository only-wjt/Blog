package com.onlyWjt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.onlyWjt.domain.dto.AddCategoryDto;
import com.onlyWjt.domain.dto.CategoryDto;
import com.onlyWjt.domain.entity.Category;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.domain.view.CategoryVo;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-05-04 15:42:11
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    List<CategoryVo> getAllCategory();

    ResponseResult getCateGoryList(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addCategory(CategoryDto categoryDto);

    ResponseResult updateCategory(AddCategoryDto addCategoryDto);

    ResponseResult getCategoryById(Long id);

    ResponseResult deleteCategoryById(Long id);
}
