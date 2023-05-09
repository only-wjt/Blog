package com.onlyWjt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.onlyWjt.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-05-09 17:17:10
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermByUserId(Long userId);
}
