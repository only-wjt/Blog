package com.onlyWjt.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserDto {
    private Long userId;
    //账号状态（0正常 1停用）
    private String status;
}
