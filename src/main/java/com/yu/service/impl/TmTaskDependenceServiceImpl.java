package com.yu.service.impl;

import com.yu.dto.TaskParam;
import com.yu.entity.TmTaskDependence;
import com.yu.mapper.TmTaskDependenceMapper;
import com.yu.service.TmTaskDependenceService;
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
 * @since 2022-05-03
 */
@Service
public class TmTaskDependenceServiceImpl extends ServiceImpl<TmTaskDependenceMapper, TmTaskDependence> implements TmTaskDependenceService {

    @Autowired
    TmTaskDependenceMapper taskDependenceMapper;

    @Override
    public List<TaskParam> getHeadTaskList(String id) {
        return taskDependenceMapper.getHeadTaskList(id);
    }
}
