package com.onlyWjt.controller;

import com.onlyWjt.domain.dto.AddRoleDto;
import com.onlyWjt.domain.dto.RoleDto;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult getRoleAllList(Integer pageNum, Integer pageSize, String roleName, String status){
        return roleService.getRoleAllList(pageNum, pageSize, roleName, status);
    }
    @PutMapping("/changeStatus")
    public ResponseResult changeRoleStatus(@RequestBody RoleDto role){
        return roleService.changeRoleStatus(role);
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody AddRoleDto roleDto){
        return roleService.addRole(roleDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getRoleById(@PathVariable("id") Long id){
        return roleService.getRoleById(id);
    }
    @DeleteMapping("{id}")
    public ResponseResult deleteRoleById(@PathVariable("id") Long id){
        return roleService.deleteRoleById(id);
    }
    @PutMapping
    public ResponseResult updateRole(@RequestBody AddRoleDto roleDto){
        return roleService.updateRole(roleDto);
    }
}
