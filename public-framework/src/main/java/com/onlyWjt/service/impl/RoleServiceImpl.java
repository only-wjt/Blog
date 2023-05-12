package com.onlyWjt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onlyWjt.constants.SystemConstants;
import com.onlyWjt.domain.entity.Menu;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.domain.entity.Role;
import com.onlyWjt.domain.view.PageVo;
import com.onlyWjt.mapper.RoleMapper;
import com.onlyWjt.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-05-09 19:17:08
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<String> selectRoleByUserId(Long userId) {
        //判断是否为管理员

        //如果是管理员，返回集合中，只需要传admin
        if(userId == 1L){
            ArrayList<String> strings = new ArrayList<>();
            strings.add("amdin");
            return strings;
        }
        //如果不是，则查询用户所有的角色信息
       return getBaseMapper().selectRoleKeyByUserId(userId);
    }

    @Override
    public ResponseResult getRoleAllList(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.like(StringUtils.hasText(roleName), Role::getRoleName,roleName);
        roleWrapper.eq(StringUtils.hasText(status),Role::getStatus, status);
        roleWrapper.orderByAsc(Role::getRoleSort);
        Page<Role> rolePage = new Page<>(pageNum,pageSize);
        page(rolePage, roleWrapper);
        PageVo pageVo = new PageVo(rolePage.getRecords(), rolePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }
}
