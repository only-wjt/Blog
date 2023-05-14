package com.onlyWjt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onlyWjt.mapper.RoleMenuMapper;
import com.onlyWjt.service.RoleMenuService;
import org.springframework.stereotype.Service;
import com.onlyWjt.domain.entity.RoleMenu;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2023-05-14 14:04:15
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}
