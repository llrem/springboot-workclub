package com.yu.service;

import com.yu.entity.TmTask;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llrem
 * @since 2022-03-11
 */
public interface TmTaskService extends IService<TmTask> {
    public List<TmTask> getTasksByBoardId(String id);
}
