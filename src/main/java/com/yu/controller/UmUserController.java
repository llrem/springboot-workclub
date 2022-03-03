package com.yu.controller;

import com.yu.common.api.Result;
import com.yu.dto.LoginParam;
import com.yu.entity.UmUser;
import com.yu.service.UmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author llrem
 * @since 2022-01-21
 */
@RestController
@RequestMapping("/user")
public class UmUserController {
    @Autowired
    UmUserService umUserService;

    @PostMapping("/register")
    public Result<UmUser> register(@RequestBody LoginParam loginParam){
        UmUser umsAdmin = umUserService.register(loginParam);
        if (umsAdmin == null) {
            return Result.failed();
        }
        return Result.success(umsAdmin);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginParam loginParam){
        String token = umUserService.login(loginParam.getUsername(), loginParam.getPassword());
        if (token == null) {
            return Result.validateFailed("用户名或密码错误！");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return Result.success(tokenMap);
    }

    @GetMapping("/getInfo")
    public Result<UmUser> getInfo(Principal principal){
        if(principal==null){
            return Result.unauthorized(null);
        }
        String username = principal.getName();
        UmUser user = umUserService.getUserByUsername(username);
        user.setPassword("");
        return Result.success(user);
    }

    @PostMapping("/logout")
    public Result logout(){
        return Result.success(null);
    }

}

