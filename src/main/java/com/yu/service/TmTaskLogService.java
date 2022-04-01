package com.yu.service;

import com.yu.dto.TaskLogParam;
import com.yu.entity.TmTaskLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llrem
 * @since 2022-04-01
 */
public interface TmTaskLogService extends IService<TmTaskLog> {
    List<TaskLogParam> getLogsByTaskId(String id);
}
