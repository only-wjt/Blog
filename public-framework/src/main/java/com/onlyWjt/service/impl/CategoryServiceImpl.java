package com.onlyWjt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onlyWjt.constants.SystemConstants;
import com.onlyWjt.domain.dto.AddCategoryDto;
import com.onlyWjt.domain.dto.CategoryDto;
import com.onlyWjt.domain.entity.Article;
import com.onlyWjt.domain.entity.Category;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.domain.view.CategoryVo;
import com.onlyWjt.domain.view.PageVo;
import com.onlyWjt.mapper.CategoryMapper;
import com.onlyWjt.service.ArticleService;
import com.onlyWjt.service.CategoryService;
import com.onlyWjt.utils.BeanCopyUtils;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-05-04 15:42:13
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //查询已发布的文章，状态已发布的文章时
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Article> eq = articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        //获取文章的分类id，并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());
        //查询分类表
        List<Category> categories = listByIds(categoryIds).stream()
                .filter(category -> category.getStatus().equals(SystemConstants.STATUS_NORMAL))
                .collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public List<CategoryVo>  getAllCategory() {
        //获取所有的已经发布的cate
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, SystemConstants.CATE_STAtUS_NORMAL);
        List<Category> list = list(wrapper);
        //将查询的数据，封装成vo返回出去
        return  BeanCopyUtils.copyBeanList(list, CategoryVo.class);
    }

    @Override
    public ResponseResult getCateGoryList(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Category> categoryWrapper = new LambdaQueryWrapper<>();
        categoryWrapper.eq(StringUtils.hasText(status), Category::getStatus, status);
        categoryWrapper.like(StringUtils.hasText(name), Category::getName, name);
        Page<Category> categoryPage = new Page<>();
        categoryPage.setCurrent(pageNum);
        categoryPage.setPages(pageSize);
        Page<Category> page = page(categoryPage, categoryWrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(page.getRecords(), CategoryVo.class);
        long total = page.getTotal();
        PageVo pageVo = new PageVo(categoryVos, total);
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addCategory(CategoryDto categoryDto) {
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateCategory(AddCategoryDto addCategoryDto) {
        updateById(BeanCopyUtils.copyBean(addCategoryDto, Category.class));
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategoryById(Long id) {
        Category byId = getById(id);
        CategoryVo categoryVo = BeanCopyUtils.copyBean(byId, CategoryVo.class);
        return ResponseResult.okResult(categoryVo);
    }

    @Override
    public ResponseResult deleteCategoryById(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }
}
