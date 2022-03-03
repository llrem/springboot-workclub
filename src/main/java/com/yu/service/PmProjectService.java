package com.yu.service;

import com.yu.entity.PmProject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llrem
 * @since 2022-02-20
 */
public interface PmProjectService extends IService<PmProject> {
    List<PmProject> getByCreateUserId(Long userId);

    List<PmProject> searchProjects(String key);
}
