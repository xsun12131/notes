package com.fatpanda.notes.pojo.entity;

import com.fatpanda.notes.common.model.config.BaseEntityListeners;
import com.fatpanda.notes.common.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners({AuditingEntityListener.class, BaseEntityListeners.class})
public class Role implements BaseEntity {

    @Id
    private String id;

    private String role;
}
