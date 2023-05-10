package com.onlyWjt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onlyWjt.domain.dto.TagListDto;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.domain.entity.Tag;
import com.onlyWjt.domain.view.PageVo;
import com.onlyWjt.mapper.TagMapper;
import com.onlyWjt.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-05-09 11:31:47
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagLambdaQueryWrapper.eq(StringUtils.hasText(tagListDto.getName()), Tag::getName,tagListDto.getName());
        tagLambdaQueryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,tagLambdaQueryWrapper);
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }
}
