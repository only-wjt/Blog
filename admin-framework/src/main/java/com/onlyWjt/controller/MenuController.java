package com.onlyWjt.controller;

import com.onlyWjt.domain.entity.Menu;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult getMenuList(String menuName, Integer status){
        return menuService.getMenuList(menuName, status);
    }
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }
    @GetMapping("{id}")
    public ResponseResult getMenuById(@PathVariable("id") String id){
        return menuService.getMenuById(id);
    }

    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }
    @DeleteMapping("{menuId}")
    public ResponseResult deleteMenuById(@PathVariable("menuId") Long menuId){
        return menuService.deleteMenuById(menuId);
    }
}
