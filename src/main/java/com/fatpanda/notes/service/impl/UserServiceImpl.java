package com.fatpanda.notes.service.impl;

import com.fatpanda.notes.common.config.security.SecurityUtils;
import com.fatpanda.notes.common.model.entity.SearchDto;
import com.fatpanda.notes.pojo.entity.Role;
import com.fatpanda.notes.pojo.entity.User;
import com.fatpanda.notes.pojo.vo.UserSimple;
import com.fatpanda.notes.repository.UserRepository;
import com.fatpanda.notes.service.RoleService;
import com.fatpanda.notes.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private RoleService roleService;

    private String encryptPassword(String password) {
        // BCryptPasswordEncoder 加密
        return new BCryptPasswordEncoder().encode(password);
    }

    @Override
    public List<User> findAll(SearchDto searchDto) {
        return null;
    }

    @Override
    public User findById(String id) {
        return null;
    }

    @Override
    public User findByName(String name) {
        return userRepository.getUserByUsername(name);
    }

    @Override
    public List<Role> getRoles(String userName) {
        List<String> roleIdList = userRepository.getRoleByUserName(userName);
        return roleService.findAllById(roleIdList);
    }

    @Override
    public User addUser(User user) {
        user.setPassword(encryptPassword(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserSimple getCurrentUserSimpleInfo() {
        String username = SecurityUtils.getUsername();
        User user = userRepository.getUserByUsername(username);
        return UserSimple.builder().username(user.getUsername()).headPic(user.getHeadPic()).build();
    }
}
