package com.fatpanda.notes.pojo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fatpanda.notes.common.model.config.BaseEntityListeners;
import com.fatpanda.notes.common.model.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author xyy
 * @date 2021/5/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@EntityListeners({AuditingEntityListener.class, BaseEntityListeners.class})
@SQLDelete(sql = "update note set del_flag = 1 where id = ?")
@SQLDeleteAll(sql = "update note set del_flag = 1 where id = ?")
@Where(clause = "del_flag = 0")
public class Note implements BaseEntity {

    @Id
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "标题")
    @NotBlank
    private String title;

    @ApiModelProperty(value = "内容")
    @NotBlank
    @Column(columnDefinition="TEXT")
    private String content;

    @ApiModelProperty(value = "摘要")
    @Column(length = 500)
    private String summary;

    @CreatedDate
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @LastModifiedDate
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标识")
    @JsonIgnore
    private boolean delFlag;

}
