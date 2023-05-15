package com.onlyWjt.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCategoryDto {
    private Long id;
    private String name;

    //描述
    private String description;
    private String status;
}
