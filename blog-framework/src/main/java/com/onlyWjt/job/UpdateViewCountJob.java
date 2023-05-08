package com.onlyWjt.job;

import com.onlyWjt.domain.entity.Article;
import com.onlyWjt.service.ArticleService;
import com.onlyWjt.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;
    @Scheduled(cron = "0/5 * * * * ?")
    public void updateViewCount(){
        log.info("同步redis数据");
        //获取redis中的数据
        Map<String, Integer> cacheMap = redisCache.getCacheMap("Article:viewCount");
        List<Article> articleList = cacheMap.entrySet().stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //更新到数据库中
        articleService.updateBatchById(articleList);
    }
}
