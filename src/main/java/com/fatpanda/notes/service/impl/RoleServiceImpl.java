package com.fatpanda.notes.service.impl;

import com.fatpanda.notes.pojo.entity.Role;
import com.fatpanda.notes.repository.RoleRepository;
import com.fatpanda.notes.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleRepository roleRepository;

    @Override
    public List<Role> findAllById(List<String> roleIdList) {
        return roleRepository.findAllById(roleIdList);
    }

}
