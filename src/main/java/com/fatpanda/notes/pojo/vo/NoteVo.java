package com.fatpanda.notes.pojo.vo;

import com.fatpanda.notes.pojo.entity.Note;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteVo {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "标签")
    private String[] tags;

    public static NoteVo byNoteAndTags(Note note, String[] tags) {
        return NoteVo.builder()
                .id(note.getId())
                .title(note.getTitle())
                .summary(note.getSummary())
                .content(note.getContent())
                .tags(tags)
                .build();
    }
}
