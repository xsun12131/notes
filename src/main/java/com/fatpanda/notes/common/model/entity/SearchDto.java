package com.fatpanda.notes.common.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto {

    private Integer PageSize;

    private Integer PageNum;

    private String query;

    private String sort;

}
