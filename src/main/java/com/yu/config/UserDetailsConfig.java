package com.yu.config;

import com.yu.service.UmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class UserDetailsConfig {
    @Autowired
    private UmUserService umUserService;

    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> umUserService.findByUsername(username);
    }
}
