package com.onlyWjt.domain.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuVo {
    private List<MenuAuthVo> menus;
    private List<Long> checkedKeys;
}
