package com.fatpanda.notes.controller;

import com.fatpanda.notes.common.model.entity.SearchDto;
import com.fatpanda.notes.common.result.annotation.ResponseResult;
import com.fatpanda.notes.pojo.entity.User;
import com.fatpanda.notes.pojo.vo.UserSimple;
import com.fatpanda.notes.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@ResponseResult
@Controller
@RequestMapping("user")
@Api(tags = "user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping
    public List<User> findAll(SearchDto searchDto) {
        return userService.findAll(searchDto);
    }

    @PostMapping
    public User addUser(User user) {
        return userService.addUser(user);
    }

    @GetMapping("currentUserSimple")
    public UserSimple getUserSimpleInfo() {
        return userService.getCurrentUserSimpleInfo();
    }


}
