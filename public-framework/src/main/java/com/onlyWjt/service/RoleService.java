package com.onlyWjt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.onlyWjt.domain.dto.AddRoleDto;
import com.onlyWjt.domain.dto.RoleDto;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-05-09 19:17:07
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleByUserId(Long userId);

    ResponseResult getRoleAllList(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult changeRoleStatus(RoleDto role);

    ResponseResult addRole(AddRoleDto roleDto);

    ResponseResult getRoleById(Long id);

    ResponseResult deleteRoleById(Long id);

    ResponseResult updateRole(AddRoleDto roleDto);
}
