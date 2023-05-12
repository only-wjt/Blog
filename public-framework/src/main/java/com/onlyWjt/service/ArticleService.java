package com.onlyWjt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.onlyWjt.domain.dto.AddArticleDto;
import com.onlyWjt.domain.entity.Article;
import com.onlyWjt.domain.entity.ResponseResult;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();
    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto article);

    ResponseResult queryArticleByTitleAndSummary(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult getArticleById(Long id);

    ResponseResult updateArticle(AddArticleDto articleDto);

    ResponseResult deleteArticleById(Long id);
}
