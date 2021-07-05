package com.fatpanda.notes.service;

import com.fatpanda.notes.common.model.entity.SearchDto;
import com.fatpanda.notes.pojo.entity.Role;
import com.fatpanda.notes.pojo.entity.User;
import com.fatpanda.notes.pojo.vo.UserSimple;

import java.util.List;

public interface UserService {

    List<User> findAll(SearchDto searchDto);

    User findById(String id);

    User findByName(String name);

    List<Role> getRoles(String userName);

    User addUser(User user);

    UserSimple getCurrentUserSimpleInfo();
}
