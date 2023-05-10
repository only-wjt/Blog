package com.onlyWjt.domain.view;

import com.onlyWjt.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouterVo {
    private List<Menu> menus;
}
