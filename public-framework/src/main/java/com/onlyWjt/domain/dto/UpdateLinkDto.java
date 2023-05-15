package com.onlyWjt.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 友链(Link)表实体类
 *
 * @author makejava
 * @since 2023-05-04 19:37:14
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLinkDto {
    private Long id;

    
    private String name;
    
    private String logo;
    
    private String description;
    //网站地址
    private String address;
    private String status;
}
