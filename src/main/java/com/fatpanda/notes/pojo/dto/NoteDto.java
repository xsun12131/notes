package com.fatpanda.notes.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "tags")
    private String[] tags;

    @ApiModelProperty(value = "标题")
    @NotBlank
    private String title;

    @ApiModelProperty(value = "内容")
    @NotBlank
    private String content;

}
