package com.yu.service.impl;

import com.yu.dto.MemberParam;
import com.yu.dto.PermissionParam;
import com.yu.entity.PmProject;
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
    public List<MemberParam> getMemberListByProjectId(String projectId) {
        return  projectUserMapper.getMemberListByProjectId(projectId);
    }

    @Override
    public List<PmProject> getProjectsByUserId(String userId) {
        return projectUserMapper.getProjectsByUserId(userId);
    }

    @Override
    public List<MemberParam> searchMember(String keyword, String projectId) {
        return projectUserMapper.searchMember(keyword,projectId);
    }

}
