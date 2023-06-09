package com.onlyWjt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onlyWjt.constants.SystemConstants;
import com.onlyWjt.domain.entity.Menu;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.domain.entity.RoleMenu;
import com.onlyWjt.domain.view.MenuAuthVo;
import com.onlyWjt.domain.view.MenuVo;
import com.onlyWjt.domain.view.RoleMenuVo;
import com.onlyWjt.enums.AppHttpCodeEnum;
import com.onlyWjt.mapper.MenuMapper;
import com.onlyWjt.service.MenuService;
import com.onlyWjt.service.RoleMenuService;
import com.onlyWjt.utils.BeanCopyUtils;
import com.onlyWjt.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-05-09 17:17:10
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private RoleMenuService roleMenuService;

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

    @Override
    public ResponseResult getMenuList(String menuName, Integer status) {
        //菜单要按照父菜单id和orderNum进行排序
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Objects.nonNull(menuName), Menu::getMenuName, menuName)
                .eq(Objects.nonNull(status), Menu::getStatus, status)
                .orderByAsc(Menu::getId, Menu::getOrderNum);
        List<Menu> list = list(queryWrapper);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(list, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenuById(Long id) {
        Menu byId = getById(id);
        MenuVo menuVo = BeanCopyUtils.copyBean(byId, MenuVo.class);
        return ResponseResult.okResult(menuVo);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        if(menu.getParentId().equals(menu.getId())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARENT_MENU_ERROR);
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenuById(Long menuId) {
        //首先需要判断是否有子菜单，如果有子菜单，则给出提示
        LambdaQueryWrapper<Menu> menuWrapper = new LambdaQueryWrapper<>();
        menuWrapper.in(Menu::getParentId, menuId);
        int count = count(menuWrapper);
        if(count>0){
            return ResponseResult.errorResult(AppHttpCodeEnum.HAS_CHILD_MENU);
        }
        removeById(menuId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTreeList() {
        MenuMapper baseMapper = getBaseMapper();
        List<Menu> allMenus = baseMapper.selectAllRouterMenu();
        //将数据转换成需要的格式
        List<MenuAuthVo> collect = allMenus.stream()
                .map(menu -> new MenuAuthVo(menu.getId(), menu.getMenuName(), menu.getParentId(), menu.getOrderNum(), menu.getPath(), menu.getMenuName(), null))
                .collect(Collectors.toList());
        //构建tree
        List<MenuAuthVo> menuTree = builderMenuAuthTree(collect,0L);
        return ResponseResult.okResult(menuTree);
    }

    @Override
    public ResponseResult getRoleMenuTreeById(Long id) {
        // TODO 待优化
        //现获取所有的权限，然后转成想要的对象
//        MenuMapper baseMapper = getBaseMapper();
        List<Menu> allMenus = baseMapper.selectAllRouterMenu();
        //查询所有的数据
        List<MenuAuthVo> menuAuthVos = allMenus.stream()
                .map(menu -> new MenuAuthVo(menu.getId(), menu.getMenuName(), menu.getParentId(), menu.getOrderNum(), menu.getPath(), menu.getMenuName(), null))
                .collect(Collectors.toList());
        List<MenuAuthVo> menuKeys = baseMapper.selectAllMenuByRoleId(id);
        if(String.valueOf(id).equals(SystemConstants.ADMIN)){
            menuKeys = menuAuthVos;
        }
        List<Long> checkedKeys = menuKeys.stream()
                .map(menuAuthVo -> menuAuthVo.getId())
                .collect(Collectors.toList());
        List<MenuAuthVo> levelMenu = builderMenuAuthTree(menuAuthVos, 0L);
        RoleMenuVo roleMenuVo = new RoleMenuVo(levelMenu, checkedKeys);
        return ResponseResult.okResult(roleMenuVo);
    }

    private List<MenuAuthVo> builderMenuAuthTree(List<MenuAuthVo> menuAuths, Long parentId) {
        List<MenuAuthVo> menuTree = menuAuths.stream()
                .filter(menuAuthVo -> menuAuthVo.getParentId().equals(parentId))
                .map(menuAuthVo -> menuAuthVo.setChildren(getAuthChildren(menuAuthVo, menuAuths)))
                .collect(Collectors.toList());
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
    private List<MenuAuthVo> getAuthChildren(MenuAuthVo menu, List<MenuAuthVo> menus) {
        List<MenuAuthVo> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getAuthChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}
