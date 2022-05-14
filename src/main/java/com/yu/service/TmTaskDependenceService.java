package com.yu.service;

import com.yu.dto.TaskParam;
import com.yu.entity.TmTaskDependence;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llrem
 * @since 2022-05-03
 */
public interface TmTaskDependenceService extends IService<TmTaskDependence> {
    List<TaskParam> getHeadTaskList(String id);
}
