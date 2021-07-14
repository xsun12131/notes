package com.fatpanda.notes.pojo.entity;

import com.fatpanda.notes.common.model.config.BaseEntityListeners;
import com.fatpanda.notes.common.model.entity.BaseEntity;
import com.fatpanda.notes.repository.NoteSeriesRepository;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

/**
 * @author xyy
 * @date 2021/7/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@EntityListeners({AuditingEntityListener.class, BaseEntityListeners.class})
@ApiModel("文章系列")
public class NoteSeries implements BaseEntity {

    @Id
    @ApiModelProperty(notes = "同noteId")
    private String id;

    private String name;

    private String parentId;

    private Integer sort;

}
