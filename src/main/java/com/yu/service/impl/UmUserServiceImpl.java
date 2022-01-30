package com.yu.service.impl;

import com.yu.common.UserDetail;
import com.yu.entity.UmUser;
import com.yu.mapper.UmUserMapper;
import com.yu.service.UmUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author llrem
 * @since 2022-01-21
 */
@Service
public class UmUserServiceImpl extends ServiceImpl<UmUserMapper, UmUser> implements UmUserService {
    @Autowired
    private UmUserMapper userMapper;

    @Override
    public UserDetails findByUsername(String username) {
        UmUser user = userMapper.findByUsername(username);
        if(user!=null){
            return new UserDetail(user);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }
}
