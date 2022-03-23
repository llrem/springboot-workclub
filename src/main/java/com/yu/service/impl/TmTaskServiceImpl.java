package com.yu.service.impl;

import com.yu.entity.TmTask;
import com.yu.mapper.TmTaskMapper;
import com.yu.service.TmTaskService;
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
 * @since 2022-03-11
 */
@Service
public class TmTaskServiceImpl extends ServiceImpl<TmTaskMapper, TmTask> implements TmTaskService {

    @Autowired
    TmTaskMapper taskMapper;

    @Override
    public List<TmTask> getTasksByBoardId(String id) {
        return taskMapper.getTasksByBoardId(id);
    }
}
