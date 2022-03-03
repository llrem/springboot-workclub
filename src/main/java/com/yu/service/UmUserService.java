package com.yu.service;

import com.yu.dto.LoginParam;
import com.yu.entity.UmUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llrem
 * @since 2022-01-21
 */
public interface UmUserService extends IService<UmUser> {
    UserDetails findByUsername(String username);

    UmUser getUserByUsername(String username);

    String login(String username,String password);

    UmUser register(LoginParam loginParam);
}
