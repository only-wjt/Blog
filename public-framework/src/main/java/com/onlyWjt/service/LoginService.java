package com.onlyWjt.service;

import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);

}
