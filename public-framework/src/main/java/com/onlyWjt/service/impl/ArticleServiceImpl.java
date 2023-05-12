package com.onlyWjt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onlyWjt.constants.SystemConstants;
import com.onlyWjt.domain.dto.AddArticleDto;
import com.onlyWjt.domain.entity.Article;
import com.onlyWjt.domain.entity.ArticleTag;
import com.onlyWjt.domain.entity.Category;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.domain.view.*;
import com.onlyWjt.mapper.ArticleMapper;
import com.onlyWjt.mapper.CategoryMapper;
import com.onlyWjt.service.ArticleService;
import com.onlyWjt.service.ArticleTagService;
import com.onlyWjt.service.CategoryService;
import com.onlyWjt.service.TagService;
import com.onlyWjt.utils.BeanCopyUtils;
import com.onlyWjt.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper  = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL).orderByDesc(Article::getViewCount);
        Page<Article> page = new Page<>(1, 10);
        page(page,queryWrapper);
        List<Article> records = page.getRecords();
        //bean拷贝
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(records, HotArticleVo.class);
        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper  = new LambdaQueryWrapper<>();
        // 如果 有categoryId 就要 查询时要和传入的相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)  && categoryId>0, Article::getCategoryId, categoryId);
        // 状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page  = new Page<>(pageNum,pageSize);
        page(page, lambdaQueryWrapper);

        //封装查询结果
        List<Article> articles = page.getRecords();
        //根据articleId去查询分类
//        for (int i = 0; i < articles.size(); i++) {
//            Category byId = categoryService.getById(articles.get(i).getCategoryId());
//            articles.get(i).setCategoryName(byId.getName());
//        }
        articles.stream()
                .forEach(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()));
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        //V1版本的，从数据库里面读取
//        Article article = getById(id);
//        //转换成VO
//        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
//        //根据分类id查询分类名
//        Long categoryId = articleDetailVo.getCategoryId();
//        Category category = categoryService.getById(categoryId);
//        if(category!=null){
//            articleDetailVo.setCategoryName(category.getName());
//        }
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        //转换成VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis对应id的浏览量
        redisCache.incrementCacheMapValue("Article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult add(AddArticleDto articleDto) {
        //首先要保存博文
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        //保存代码，保存之后，会自动把id进去
        save(article);
        //此时要根据id去保存cate的信息
        List<ArticleTag> collect = articleDto.getTags().stream()
                .map(dto -> new ArticleTag(article.getId(), dto))
                .collect(Collectors.toList());
        articleTagService.saveBatch(collect);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult queryArticleByTitleAndSummary(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> articleWarpper = new LambdaQueryWrapper<>();
        articleWarpper.like(StringUtils.hasText(title), Article::getTitle, title);
        articleWarpper.like(StringUtils.hasText(summary), Article::getSummary, summary);
        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        page(articlePage,articleWarpper);
        List<Article> records = articlePage.getRecords();
        PageVo pageVo = new PageVo(records, articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleById(Long id) {
        //根据id获取文章内容
        Article article = articleService.getById(id);
        ArticleVo articleVo = BeanCopyUtils.copyBean(article, ArticleVo.class);
        //获取到文章之后，使用文章id获取tag数组
        LambdaQueryWrapper<ArticleTag> articleTagWrapper = new LambdaQueryWrapper<>();
        articleTagWrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> list = articleTagService.list(articleTagWrapper);
        List<Long> tags = list.stream()
                .map(tag -> tag.getTagId())
                .collect(Collectors.toList());
        articleVo.setTags(tags);
        return ResponseResult.okResult(articleVo);
    }

    @Override
    @Transactional
    public ResponseResult updateArticle(AddArticleDto articleDto) {
        //先更新文章
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);
        //在更新tags，tag先删除，后新增
        List<Long> tags = articleDto.getTags();
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, articleDto.getId());
        articleTagService.remove(articleTagLambdaQueryWrapper);
        List<ArticleTag> collect = articleDto.getTags().stream()
                .map(dto -> new ArticleTag(article.getId(), dto))
                .collect(Collectors.toList());
        articleTagService.saveBatch(collect);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticleById(Long id) {
        articleMapper.deleteById(id);
        return ResponseResult.okResult();
    }
}
