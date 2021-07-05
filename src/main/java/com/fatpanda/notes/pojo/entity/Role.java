package com.fatpanda.notes.pojo.entity;

import com.fatpanda.notes.common.model.config.BaseEntityListeners;
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
@SQLDelete(sql = "update note set del_flag = 1 where id = ?")
@SQLDeleteAll(sql = "update note set del_flag = 1 where id = ?")
@Where(clause = "del_flag = 0")
public class Role {

    @Id
    private String id;

    private String role;

    private Boolean delFlag;
}
