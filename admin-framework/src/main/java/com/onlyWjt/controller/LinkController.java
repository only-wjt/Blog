package com.onlyWjt.controller;

import com.onlyWjt.domain.dto.AddLinkDto;
import com.onlyWjt.domain.dto.LinkStatusDto;
import com.onlyWjt.domain.dto.UpdateLinkDto;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;
    @GetMapping("/list")
    public ResponseResult getLinkList(Integer pageNum, Integer pageSize, String name, String status){
        return linkService.getLinkList(pageNum, pageSize, name, status);
    }

    @PostMapping
    public ResponseResult saveLink(@RequestBody AddLinkDto linkDto){
        return linkService.saveLink(linkDto);
    }

    @GetMapping("{id}")
    public ResponseResult getLinkbyId(@PathVariable("id") Long id){
        return linkService.getLinkById(id);
    }

    @PutMapping
    public ResponseResult updateLink(@RequestBody UpdateLinkDto linkDto){
        return linkService.updateLink(linkDto);
    }
    @DeleteMapping("{id}")
    public ResponseResult deleteLinkById(@PathVariable("id") Long id){
        return linkService.deleteLinkById(id);
    }
    @PutMapping("/changeLinkStatus")
    public ResponseResult changeLinkStatus(@RequestBody LinkStatusDto statusDto){
        return linkService.changeLinkStatus(statusDto);
    }
}
