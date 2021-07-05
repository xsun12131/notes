package com.fatpanda.notes.controller;

import com.fatpanda.notes.common.result.entity.Result;
import com.fatpanda.notes.pojo.dto.UserLogin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 登录控制器
 *
 * @author Louis
 * @date Jun 29, 2019
 */
@RestController
public class LoginController {

    /**
     * 登录接口
     */
    @PostMapping(value = "/login")
    public Result login(@RequestBody UserLogin loginBean, HttpServletRequest request) throws IOException {
        return Result.OK();
    }

}