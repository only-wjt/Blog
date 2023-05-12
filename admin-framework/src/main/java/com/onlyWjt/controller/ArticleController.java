package com.onlyWjt.controller;

import com.onlyWjt.domain.dto.AddArticleDto;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }

    @GetMapping("/list")
    public ResponseResult getArticleList(Integer pageNum,Integer pageSize, String title,String summary){
        return articleService.queryArticleByTitleAndSummary(pageNum,pageSize,title,summary);
    }
    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable("id") Long id){
        return articleService.getArticleById(id);
    }

    @PutMapping
    public ResponseResult updateArticle(@RequestBody AddArticleDto articleDto){
        return articleService.updateArticle(articleDto);
    }
    @DeleteMapping("{id}")
    public ResponseResult deleteArticleById(@PathVariable("id") Long id){
        return articleService.deleteArticleById(id);
    }
}
