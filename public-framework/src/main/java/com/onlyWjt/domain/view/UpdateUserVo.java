package com.onlyWjt.domain.view;

import com.onlyWjt.domain.entity.Role;
import com.onlyWjt.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserVo {
    private List<Long> roleIds;
    private List<Role> roles;
    private User user;
}
