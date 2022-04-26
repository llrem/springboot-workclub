package com.yu.service.impl;

import com.yu.dto.MemberParam;
import com.yu.entity.TmTask;
import com.yu.entity.TmTaskFollower;
import com.yu.entity.UmUser;
import com.yu.mapper.TmTaskFollowerMapper;
import com.yu.service.TmTaskFollowerService;
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
 * @since 2022-03-24
 */
@Service
public class TmTaskFollowerServiceImpl extends ServiceImpl<TmTaskFollowerMapper, TmTaskFollower> implements TmTaskFollowerService {

    @Autowired
    TmTaskFollowerMapper taskFollowerMapper;

    @Override
    public List<MemberParam> getFollowersByTaskId(String taskId) {
        return taskFollowerMapper.getFollowersByTaskId(taskId);
    }

    @Override
    public List<TmTask> getTaskByUserId(String userId) {
        return taskFollowerMapper.getTaskByUserId(userId);
    }
}
