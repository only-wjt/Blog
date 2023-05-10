package com.onlyWjt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.DeleteById;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onlyWjt.domain.dto.TagListDto;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.domain.entity.Tag;
import com.onlyWjt.domain.view.PageVo;
import com.onlyWjt.enums.AppHttpCodeEnum;
import com.onlyWjt.exception.SystemException;
import com.onlyWjt.mapper.TagMapper;
import com.onlyWjt.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private TagMapper tagMapper;

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

    @Override
    public ResponseResult addTag(Tag tag) {
        if(!StringUtils.hasText(tag.getName())){
            throw new SystemException(AppHttpCodeEnum.TAG_NAME_NULL);
        }
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTagById(String tagId) {
        tagMapper.deleteById(tagId);
        return ResponseResult.okResult();
    }
}
