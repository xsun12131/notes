package com.fatpanda.notes.common.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto {

    @ApiModelProperty(example = "10")
    private Integer pageSize;

    @ApiModelProperty(example = "1")
    private Integer pageNum;

    private String query;

    private String sort;

}
