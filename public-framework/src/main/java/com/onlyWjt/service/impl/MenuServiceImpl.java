package com.onlyWjt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onlyWjt.constants.SystemConstants;
import com.onlyWjt.domain.entity.Menu;
import com.onlyWjt.mapper.MenuMapper;
import com.onlyWjt.service.MenuService;
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
        if(userId == 1L){
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
}
