package com.yu.service;

import com.yu.dto.MemberParam;
import com.yu.entity.PmProject;
import com.yu.entity.PmProjectUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llrem
 * @since 2022-03-22
 */
public interface PmProjectUserService extends IService<PmProjectUser> {
    List<MemberParam> getMemberListByProjectId(Long projectId);
    List<PmProject> getProjectsByUserId(Long userId);
}
