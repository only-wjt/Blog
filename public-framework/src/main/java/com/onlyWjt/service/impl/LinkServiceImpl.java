package com.onlyWjt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onlyWjt.constants.SystemConstants;
import com.onlyWjt.domain.dto.AddLinkDto;
import com.onlyWjt.domain.dto.LinkStatusDto;
import com.onlyWjt.domain.dto.UpdateLinkDto;
import com.onlyWjt.domain.entity.Link;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.domain.view.LinkVo;
import com.onlyWjt.domain.view.PageVo;
import com.onlyWjt.mapper.LinkMapper;
import com.onlyWjt.service.LinkService;
import com.onlyWjt.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-05-04 19:37:18
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(queryWrapper);
        //转换成vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        //封装返回
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult getLinkList(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Link> linkWarpper = new LambdaQueryWrapper<>();
        linkWarpper.eq(StringUtils.hasText(status), Link::getStatus, status);
        linkWarpper.like(StringUtils.hasText(name),Link::getName, name);
        Page<Link> linkPage = new Page<>(pageNum, pageSize);
        Page<Link> page = page(linkPage, linkWarpper);
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(page.getRecords(), LinkVo.class);
        long total = page.getTotal();
        return ResponseResult.okResult(new PageVo(linkVos, total));
    }

    @Override
    public ResponseResult saveLink(AddLinkDto linkDto) {
        Link link = BeanCopyUtils.copyBean(linkDto, Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getLinkById(Long id) {
        Link byId = getById(id);
        return ResponseResult.okResult(byId);
    }

    @Override
    public ResponseResult updateLink(UpdateLinkDto linkDto) {
        updateById(BeanCopyUtils.copyBean(linkDto, Link.class));
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLinkById(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeLinkStatus(LinkStatusDto statusDto) {
        Link link = BeanCopyUtils.copyBean(statusDto, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }

}
