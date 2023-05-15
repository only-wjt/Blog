package com.onlyWjt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.onlyWjt.domain.dto.AddLinkDto;
import com.onlyWjt.domain.dto.LinkStatusDto;
import com.onlyWjt.domain.dto.UpdateLinkDto;
import com.onlyWjt.domain.entity.Link;
import com.onlyWjt.domain.entity.ResponseResult;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-05-04 19:37:16
 */
public interface LinkService extends IService<Link> {
    ResponseResult getAllLink();

    ResponseResult getLinkList(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult saveLink(AddLinkDto linkDto);

    ResponseResult getLinkById(Long id);

    ResponseResult updateLink(UpdateLinkDto linkDto);

    ResponseResult deleteLinkById(Long id);

    ResponseResult changeLinkStatus(LinkStatusDto statusDto);
}
