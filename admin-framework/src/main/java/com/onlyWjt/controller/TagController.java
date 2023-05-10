package com.onlyWjt.controller;

import com.onlyWjt.domain.dto.TagListDto;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.domain.view.PageVo;
import com.onlyWjt.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
