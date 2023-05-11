package com.onlyWjt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onlyWjt.domain.entity.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-09 17:17:06
 */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermByUserId(Long userId);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterTreeMenuByUserId(Long userId);
}
