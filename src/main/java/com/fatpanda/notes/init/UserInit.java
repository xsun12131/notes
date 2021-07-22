package com.fatpanda.notes.init;

import com.fatpanda.notes.pojo.entity.User;
import com.fatpanda.notes.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author xyy
 * @date 2021/7/8
 *
 * 用户初始化
 */
@Slf4j
@Configuration
public class UserInit implements CommandLineRunner {

    @Resource
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        User admin = userService.findByName("admin");
        if (admin != null) {
            return;
        }

        admin = User.builder().username("admin").password("passwd").build();
        userService.addUser(admin);
    }

}
