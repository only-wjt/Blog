package com.onlyWjt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onlyWjt.domain.dto.AddUserDto;
import com.onlyWjt.domain.dto.ChangeUserDto;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.domain.entity.User;
import com.onlyWjt.domain.view.PageVo;
import com.onlyWjt.domain.view.UserInfoVo;
import com.onlyWjt.enums.AppHttpCodeEnum;
import com.onlyWjt.exception.SystemException;
import com.onlyWjt.mapper.UserMapper;
import com.onlyWjt.service.UserService;
import com.onlyWjt.utils.BeanCopyUtils;
import com.onlyWjt.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-05-06 16:55:43
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult userInfo() {
        //获取用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User byId = getById(userId);
        //封装成userInfo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(byId, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //...
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getAllUserList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(StringUtils.hasText(status),User::getStatus, status);
        userWrapper.like(StringUtils.hasText(userName),User::getUserName, userName);
        userWrapper.like(StringUtils.hasText(phonenumber),User::getPhonenumber, phonenumber);

        Page<User> userPage = new Page<>(pageNum, pageSize);
        page(userPage, userWrapper);
        List<User> records = userPage.getRecords();
        long total = userPage.getTotal();
        return ResponseResult.okResult(new PageVo(records,total));
    }

    @Override
    public ResponseResult changeUserStatus(ChangeUserDto userDto) {
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        user.setId(userDto.getUserId());
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addUser(AddUserDto userDto) {
        if(StringUtils.hasText(userDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        return null;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getNickName,nickName);
        return count(userLambdaQueryWrapper)>0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserName,userName);
        return count(userLambdaQueryWrapper)>0;
    }
}
