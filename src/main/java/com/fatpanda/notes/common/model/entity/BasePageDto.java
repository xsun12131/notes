package com.fatpanda.notes.common.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xyy
 * @date 2021/7/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasePageDto {

    @ApiModelProperty(example = "10")
    private Integer pageSize;

    @ApiModelProperty(example = "1")
    private Integer pageNum;

}
