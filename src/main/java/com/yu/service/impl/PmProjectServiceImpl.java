package com.yu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yu.dto.PieDataParam;
import com.yu.entity.PmProject;
import com.yu.entity.UmUser;
import com.yu.mapper.PmProjectMapper;
import com.yu.service.PmProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.coyote.ajp.AjpProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author llrem
 * @since 2022-02-20
 */
@Service
public class PmProjectServiceImpl extends ServiceImpl<PmProjectMapper, PmProject> implements PmProjectService {
    @Autowired
    private PmProjectMapper projectMapper;

    @Override
    public List<PmProject> getByCreateUserId(Long userId) {
        QueryWrapper<PmProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_user_id",userId);
        return projectMapper.selectList(queryWrapper);
    }

    @Override
    public List<PmProject> searchProjects(String key) {
        QueryWrapper<PmProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",key);
        return projectMapper.selectList(queryWrapper);
    }

    @Override
    public List<PieDataParam> getProjectsData(String userId) {
        return projectMapper.getProjectsData(userId);
    }

    @Override
    public List<String> getStatusData(String userId) {
        return projectMapper.getStatusData(userId);
    }

    @Override
    public List<String> getPriorityData(String userId) {
        return projectMapper.getPriorityData(userId);
    }
}
