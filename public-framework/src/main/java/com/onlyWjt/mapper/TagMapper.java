package com.onlyWjt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onlyWjt.domain.entity.Tag;
import org.springframework.stereotype.Repository;


/**
 * 标签(Tag)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-09 11:31:41
 */
@Repository
public interface TagMapper extends BaseMapper<Tag> {

}
