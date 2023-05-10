package com.onlyWjt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onlyWjt.constants.SystemConstants;
import com.onlyWjt.domain.entity.Menu;
import com.onlyWjt.mapper.MenuMapper;
import com.onlyWjt.service.MenuService;
import com.onlyWjt.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-05-09 17:17:10
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermByUserId(Long userId) {
        //如果是管理员，则返回所有的权限
        if(SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<Menu>();
            menuLambdaQueryWrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            menuLambdaQueryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<String> collect = list(menuLambdaQueryWrapper).stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return collect;
        }
        //否则返回所具有的全新啊
        return getBaseMapper().selectPermByUserId(userId);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper baseMapper = getBaseMapper();
        List<Menu> allMenus = null;
        //判断是否管理员
        //是，返回所有符合要求的menus
        if(SecurityUtils.isAdmin()){
            allMenus = baseMapper.selectAllRouterMenu();
        }else{
            //如果不是管理员，则返回当前用户所具有的menus
            allMenus = baseMapper.selectRouterTreeMenuByUserId(userId);
        }
        //构建tree
        List<Menu> menuTree = builderMenuTree(allMenus,0L);
        return menuTree;
    }

    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     *
     * @param
     * @param menu
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}
