package com.fatpanda.notes.pojo.dto;

import com.fatpanda.notes.common.model.entity.SearchDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xyy
 * @date 2021/7/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoteSearchDto extends SearchDto {

    private String[] tags;

    private String series;

}
