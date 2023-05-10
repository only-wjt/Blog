package com.onlyWjt.controller;

import com.onlyWjt.domain.entity.LoginUser;
import com.onlyWjt.domain.entity.Menu;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.domain.entity.User;
import com.onlyWjt.domain.view.AdminUserInfoVo;
import com.onlyWjt.domain.view.RouterVo;
import com.onlyWjt.domain.view.UserInfoVo;
import com.onlyWjt.enums.AppHttpCodeEnum;
import com.onlyWjt.exception.SystemException;
import com.onlyWjt.service.LoginService;
import com.onlyWjt.service.MenuService;
import com.onlyWjt.service.RoleService;
import com.onlyWjt.utils.BeanCopyUtils;
import com.onlyWjt.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        Long userId = loginUser.getUser().getId();
        List<String> perms=  menuService.selectPermByUserId(userId);
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleByUserId(userId);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        //封装数据返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roleKeyList,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }
    @GetMapping("/getRouters")
    public ResponseResult<RouterVo> getRoutes(){
        //查询menus，结果是嵌套tree的形式
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询权限信息
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RouterVo(menus));
    }
    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }

}
