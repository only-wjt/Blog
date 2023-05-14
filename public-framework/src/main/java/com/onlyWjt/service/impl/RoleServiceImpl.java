package com.onlyWjt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.SelectById;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onlyWjt.constants.SystemConstants;
import com.onlyWjt.domain.dto.AddRoleDto;
import com.onlyWjt.domain.dto.RoleDto;
import com.onlyWjt.domain.entity.Menu;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.domain.entity.Role;
import com.onlyWjt.domain.entity.RoleMenu;
import com.onlyWjt.domain.view.PageVo;
import com.onlyWjt.domain.view.RoleVo;
import com.onlyWjt.enums.AppHttpCodeEnum;
import com.onlyWjt.mapper.RoleMapper;
import com.onlyWjt.service.RoleMenuService;
import com.onlyWjt.service.RoleService;
import com.onlyWjt.utils.BeanCopyUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-05-09 19:17:08
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;
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

    @Override
    public ResponseResult changeRoleStatus(RoleDto role) {
        Role role1 = BeanCopyUtils.copyBean(role, Role.class);
        role1.setId(role.getRoleId());
        updateById(role1);
        return ResponseResult.okResult();
    }

    @Override
    //要加事务
    @Transactional
    public ResponseResult addRole(AddRoleDto roleDto) {
        //首先要保存成role数据
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        save(role);
        //然后封装成menu数据，保存
        List<RoleMenu> roleMenus = roleDto.getMenuIds().stream()
                .map(menuid -> new RoleMenu(role.getId(), menuid))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRoleById(Long id) {
        Role role = getById(id);
        if (Objects.isNull(role)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.ROLE_NOT_EXIT);
        }
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    public ResponseResult deleteRoleById(Long id) {
        //删除超级
        if (String.valueOf(id).equals(SystemConstants.ADMIN)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CAN_NOT_DELETE_ADMIN);
        }
        baseMapper.deleteRoleMenuByRoleId(id);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult updateRole(AddRoleDto roleDto) {
        //先根据删除关联关系表的数据
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        updateById(role);
        List<Long> menuIds = roleDto.getMenuIds();
        if (menuIds != null && menuIds.size() > 0) {
            baseMapper.deleteRoleMenuByRoleId(role.getId());
            baseMapper.insertRoleMenu(menuIds, role.getId());
        }else {
            baseMapper.deleteRoleMenuByRoleId(role.getId());
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRoleAllList() {
        LambdaQueryWrapper<Role> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> list = list(roleWrapper);
        return ResponseResult.okResult(list);
    }
}
