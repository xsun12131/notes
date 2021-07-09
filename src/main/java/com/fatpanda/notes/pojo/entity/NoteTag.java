package com.fatpanda.notes.pojo.entity;

import com.fatpanda.notes.common.model.config.BaseEntityListeners;
import com.fatpanda.notes.common.model.entity.BaseEntity;
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
 * @date 2021/7/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@EntityListeners({AuditingEntityListener.class, BaseEntityListeners.class})
public class NoteTag implements BaseEntity {

    @Id
    private String id;

    private String name;

    private String parentId;

}
