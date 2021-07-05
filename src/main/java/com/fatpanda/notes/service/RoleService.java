package com.fatpanda.notes.service;

import com.fatpanda.notes.pojo.entity.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAllById(List<String> roleIdList);

}
