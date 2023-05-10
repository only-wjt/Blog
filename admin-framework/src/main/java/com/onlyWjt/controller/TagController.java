package com.onlyWjt.controller;

import com.onlyWjt.domain.dto.TagDto;
import com.onlyWjt.domain.dto.TagListDto;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.domain.entity.Tag;
import com.onlyWjt.domain.view.PageVo;
import com.onlyWjt.service.TagService;
import com.onlyWjt.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

//    @GetMapping("/list")
//    public ResponseResult list(){
//        return ResponseResult.okResult(tagService.list());
//    }

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

   @PostMapping()
    public ResponseResult addTag(@RequestBody TagDto tagDto){
       Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
       return tagService.addTag(tag);
    }
    @DeleteMapping("{id}")
    public ResponseResult deleteTagById(@PathVariable("id") String tagId){

        return tagService.deleteTagById(tagId);
    }

}
