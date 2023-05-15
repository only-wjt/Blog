package com.onlyWjt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onlyWjt.domain.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-05 12:52:28
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
    boolean insertUserRole(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    List<Long> getRoleIdsByUserId(Long userId);

    void deleteRoleByUserId(Long id);
}
