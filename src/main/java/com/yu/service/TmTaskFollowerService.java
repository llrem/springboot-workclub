package com.yu.service;

import com.yu.dto.MemberParam;
import com.yu.entity.TmTask;
import com.yu.entity.TmTaskFollower;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yu.entity.UmUser;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llrem
 * @since 2022-03-24
 */
public interface TmTaskFollowerService extends IService<TmTaskFollower> {
    List<MemberParam> getFollowersByTaskId(String taskId);

    List<TmTask> getTaskByUserId(String userId);
}
