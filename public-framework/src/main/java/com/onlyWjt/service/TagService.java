package com.onlyWjt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.onlyWjt.domain.dto.AddTagDto;
import com.onlyWjt.domain.dto.TagListDto;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.domain.entity.Tag;
import com.onlyWjt.domain.view.PageVo;
import com.onlyWjt.domain.view.TagVo;

import java.util.List;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-05-09 11:31:45
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult deleteTagById(String tagId);

    ResponseResult getTagById(Long tagId);

    ResponseResult updateTagById(AddTagDto tag);

    List<TagVo> getAllTag();
}
