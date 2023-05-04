package com.onlyWjt.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
}
