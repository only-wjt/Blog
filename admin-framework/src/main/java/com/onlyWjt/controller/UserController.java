package com.onlyWjt.controller;

import com.onlyWjt.domain.dto.AddUserDto;
import com.onlyWjt.domain.dto.ChangeUserDto;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseResult getAllUserList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status){
        return userService.getAllUserList(pageNum,pageSize,userName,phonenumber,status);
    }
    @PutMapping("/changeStatus")
    public ResponseResult changeUserStatus(@RequestBody ChangeUserDto userDto){
        return userService.changeUserStatus(userDto);
    }
    @PostMapping
    public ResponseResult addUser(@RequestBody AddUserDto userDto){
        return userService.addUser(userDto);
    }
}
