package com.onlyWjt.controller;

import com.onlyWjt.constants.SystemConstants;
import com.onlyWjt.domain.dto.AddCommentDto;
import com.onlyWjt.domain.entity.Comment;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.service.CommentService;
import com.onlyWjt.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论",description = "评论相关接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT, articleId, pageNum, pageSize);
    }

    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto comment) {
        Comment comment1 = BeanCopyUtils.copyBean(comment, Comment.class);
        return commentService.addComment(comment1);
    }

    @GetMapping("/linkCommentList")
    @ApiOperation(value = "获取评论列表", notes = "获取一页的评论数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "页号"),
            @ApiImplicitParam(name = "pageSize",value = "每页大小")
    })
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize) {
        return commentService.commentList(SystemConstants.LINK_COMMENT, null, pageNum, pageSize);
    }
}