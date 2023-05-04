package com.onlyWjt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.onlyWjt.domain.entity.Category;
import com.onlyWjt.domain.entity.ResponseResult;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-05-04 15:42:11
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();
}
