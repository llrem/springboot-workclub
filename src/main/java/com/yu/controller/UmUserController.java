package com.yu.controller;

import com.yu.common.Result;
import com.yu.dto.LoginParam;
import com.yu.entity.UmUser;
import com.yu.service.UmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author llrem
 * @since 2022-01-21
 */
@RestController
@RequestMapping
public class UmUserController {
    @Autowired
    UmUserService umUserService;

    @GetMapping("/hello")
    public String hello(){
        return "hello!!!";
    }

    @GetMapping("/")
    public String no(){
        String encoded = new BCryptPasswordEncoder().encode("123456");
        return encoded;
    }
}

