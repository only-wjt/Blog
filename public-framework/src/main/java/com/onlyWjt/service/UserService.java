package com.onlyWjt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.onlyWjt.domain.dto.AddUserDto;
import com.onlyWjt.domain.dto.ChangeUserDto;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-05-06 16:55:42
 */
public interface UserService extends IService<User> {
    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult getAllUserList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult changeUserStatus(ChangeUserDto userDto);

    ResponseResult addUser(AddUserDto userDto);
}
