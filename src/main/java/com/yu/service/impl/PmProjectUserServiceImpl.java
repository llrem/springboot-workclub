package com.yu.service.impl;

import com.yu.dto.MemberParam;
import com.yu.entity.PmProjectUser;
import com.yu.mapper.PmProjectUserMapper;
import com.yu.service.PmProjectUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author llrem
 * @since 2022-03-22
 */
@Service
public class PmProjectUserServiceImpl extends ServiceImpl<PmProjectUserMapper, PmProjectUser> implements PmProjectUserService {

    @Autowired
    private PmProjectUserMapper projectUserMapper;
    @Override
    public List<MemberParam> getMemberListByProjectId(Long projectId) {
        return  projectUserMapper.getMemberListByProjectId(projectId);
    }
}
