package com.yu.service.impl;

import com.yu.dto.SubTaskParam;
import com.yu.entity.TmTaskSub;
import com.yu.mapper.TmTaskSubMapper;
import com.yu.service.TmTaskSubService;
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
 * @since 2022-05-02
 */
@Service
public class TmTaskSubServiceImpl extends ServiceImpl<TmTaskSubMapper, TmTaskSub> implements TmTaskSubService {
    @Autowired
    TmTaskSubMapper taskSubMapper;

    @Override
    public List<SubTaskParam> getSubTasks(String taskId) {
        return taskSubMapper.getSubTasks(taskId);
    }
}
