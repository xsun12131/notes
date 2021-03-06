package com.fatpanda.notes.common.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fatpanda
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto extends BasePageDto{

    private String query;

    @ApiModelProperty()
    private String sort;

    @ApiModelProperty(example = "DESC")
    private String sortRule;

}
