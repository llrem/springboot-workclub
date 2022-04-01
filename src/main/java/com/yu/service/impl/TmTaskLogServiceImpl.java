package com.yu.service.impl;

import com.yu.dto.TaskLogParam;
import com.yu.entity.TmTaskLog;
import com.yu.mapper.TmTaskLogMapper;
import com.yu.service.TmTaskLogService;
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
 * @since 2022-04-01
 */
@Service
public class TmTaskLogServiceImpl extends ServiceImpl<TmTaskLogMapper, TmTaskLog> implements TmTaskLogService {
    @Autowired
    TmTaskLogMapper taskLogMapper;

    @Override
    public List<TaskLogParam> getLogsByTaskId(String id) {
        return taskLogMapper.getLogsByTaskId(id);
    }
}
