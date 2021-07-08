package com.fatpanda.notes.pojo.esEntity;

import com.fatpanda.notes.common.model.entity.BaseEntity;
import com.fatpanda.notes.pojo.entity.Note;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author xyy
 * @date 2021/5/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "notes")
public class EsNote implements BaseEntity {

    @Id
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "标题")
    @NotBlank
    @Field(type = FieldType.Text, analyzer = "ik_max_word", store = true)
    private String title;

    @ApiModelProperty(value = "内容")
    @NotBlank
    @Field(type = FieldType.Text, analyzer = "ik_max_word", store = true)
    private String content;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "创建时间")
    @Field(type = FieldType.Date)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @Field(type = FieldType.Date)
    private LocalDateTime updateTime;

    public static EsNote byNote(Note note) {
     return EsNote.builder()
             .id(note.getId())
             .title(note.getTitle())
             .content(note.getContent())
             .summary(note.getSummary())
             .updateTime(note.getUpdateTime())
             .createTime(note.getCreateTime())
             .build();
    }
}
