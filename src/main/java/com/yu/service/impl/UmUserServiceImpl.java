package com.yu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yu.common.UserDetail;
import com.yu.common.exception.Asserts;
import com.yu.dto.LoginParam;
import com.yu.entity.UmUser;
import com.yu.mapper.UmUserMapper;
import com.yu.service.UmUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public UserDetails findByUsername(String username) {
        UmUser user = userMapper.findByUsername(username);
        if(user!=null){
            return new UserDetail(user);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public UmUser getUserByUsername(String username) {
        QueryWrapper<UmUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public String login(String username, String password) {
        String token = null;
            try{
                UserDetails userDetails = findByUsername(username);
                if(!passwordEncoder.matches(password,userDetails.getPassword())){
                    Asserts.fail("密码不正确");
                }
                if(!userDetails.isEnabled()){
                    Asserts.fail("帐号已被禁用");
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                token = jwtTokenUtil.generateToken(userDetails);
            } catch (AuthenticationException e) {
                System.out.println(e.getMessage());
            }
        return token;
    }

    @Override
    public UmUser register(LoginParam loginParam) {
        UmUser user = new UmUser();
        user.setUsername(loginParam.getUsername());
        user.setStatus(1);
        QueryWrapper<UmUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmUser::getUsername,user.getUsername());
        List<UmUser> umsAdminList = list(wrapper);
        if (umsAdminList.size() > 0) {
            return null;
        }
        String encodePassword = passwordEncoder.encode(loginParam.getPassword());
        user.setPassword(encodePassword);
        baseMapper.insert(user);
        return user;
    }
}
