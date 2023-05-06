package com.onlyWjt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onlyWjt.domain.entity.User;
import org.springframework.stereotype.Repository;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-05 12:52:28
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

}
