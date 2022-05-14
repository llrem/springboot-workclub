package com.yu.service;

import com.yu.dto.SubTaskParam;
import com.yu.entity.TmTaskSub;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llrem
 * @since 2022-05-02
 */
public interface TmTaskSubService extends IService<TmTaskSub> {
    List<SubTaskParam> getSubTasks(String taskId);
}
