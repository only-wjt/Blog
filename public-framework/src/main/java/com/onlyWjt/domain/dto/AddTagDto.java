package com.onlyWjt.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddTagDto {
    private String id;
    private String name;
    private String remark;

}
