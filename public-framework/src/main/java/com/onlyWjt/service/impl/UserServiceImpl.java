package com.onlyWjt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onlyWjt.domain.entity.User;
import com.onlyWjt.mapper.UserMapper;
import com.onlyWjt.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-05-06 16:55:43
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
