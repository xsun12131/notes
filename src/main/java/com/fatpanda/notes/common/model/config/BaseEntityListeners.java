package com.fatpanda.notes.common.model.config;


import com.fatpanda.notes.common.model.entity.BaseEntity;
import com.fatpanda.notes.common.utils.IdUtil;
import com.fatpanda.notes.common.utils.StringUtil;

import javax.persistence.PrePersist;

/**
 * @author xyy
 * @date 2021/5/18
 */
public class BaseEntityListeners {

    @PrePersist
    public void PrePersist(Object entity) {
        if (!(entity instanceof BaseEntity)) {
            return;
        }
        BaseEntity baseEntity = (BaseEntity) entity;
        if (StringUtil.isBlank(baseEntity.getId())) {
            baseEntity.setId(IdUtil.getShortId());
        }
    }

}
