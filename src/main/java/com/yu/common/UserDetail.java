package com.yu.common;

import com.yu.entity.UmUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class UserDetail implements UserDetails {
    private UmUser umUser;

    public UserDetail(UmUser umUser){
        this.umUser = umUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(umUser.getId()+":"+umUser.getUsername()));
    }

    public UmUser getUser(){
        return umUser;
    }

    @Override
    public String getPassword() {
        return umUser.getPassword();
    }

    @Override
    public String getUsername() {
        return umUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return umUser.getStatus().equals(1);
    }
}
