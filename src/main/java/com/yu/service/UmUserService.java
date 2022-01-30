package com.yu.service;

import com.yu.common.UserDetail;
import com.yu.entity.UmUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
}
