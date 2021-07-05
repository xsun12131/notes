package com.fatpanda.notes.common.result.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author fatPanda
 * @date 2020/10/14
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private ResponseResultInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }

}
