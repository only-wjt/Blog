package com.onlyWjt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onlyWjt.domain.entity.ArticleTag;
import com.onlyWjt.mapper.ArticleTagMapper;
import com.onlyWjt.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2023-05-11 18:28:39
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
