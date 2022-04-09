package com.yu.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
public class UserController {
    @Autowired
    UmUserService userService;

    @PostMapping("/register")
    public Result<UmUser> register(@RequestBody LoginParam loginParam){
        UmUser umsAdmin = userService.register(loginParam);
        if (umsAdmin == null) {
            return Result.failed();
        }
        return Result.success(umsAdmin);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginParam loginParam){
        String token = userService.login(loginParam.getUsername(), loginParam.getPassword());
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
        UmUser user = userService.getUserByUsername(username);
        user.setPassword("");
        return Result.success(user);
    }

    @PostMapping("/logout")
    public Result logout(){
        return Result.success(null);
    }

    @PostMapping("/saveInfo")
    public Result saveInfo(@RequestBody UmUser user){
        UpdateWrapper<UmUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",user.getId())
                .set("nick_name",user.getNickName())
                .set("gender",user.getGender())
                .set("age",user.getAge())
                .set("email",user.getEmail())
                .set("phone",user.getPhone())
                .set("job",user.getJob())
                .set("city",user.getCity())
                .set("personalized_signature",user.getPersonalizedSignature());
        boolean isUpdate = userService.update(updateWrapper);
        if(isUpdate){
            return Result.success(user);
        }
        return Result.failed();
    }

    @PostMapping("/save_avatar")
    public Result saveAvatar(@RequestBody UmUser user){
        UpdateWrapper<UmUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",user.getId())
                .set("icon",user.getIcon());
        boolean isUpdate = userService.update(updateWrapper);
        if(isUpdate){
            return Result.success(user);
        }
        return Result.failed();
    }
}

