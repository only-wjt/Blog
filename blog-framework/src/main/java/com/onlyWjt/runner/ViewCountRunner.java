package com.onlyWjt.runner;

import com.onlyWjt.domain.entity.Article;
import com.onlyWjt.mapper.ArticleMapper;
import com.onlyWjt.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        log.info("程序启动后");
        //查询博客信息 id 和viewcout
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> String.valueOf(article.getId()), article -> article.getViewCount().intValue()));

        redisCache.setCacheMap("Article:viewCount",viewCountMap);
    }
    @PreDestroy
    public void destory(){
        log.info("在程序关闭前执行");
    }
}
