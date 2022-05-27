package com.yu.service;

import com.yu.dto.PieDataParam;
import com.yu.entity.PmProject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yu.entity.PmTaskUndone;
import com.yu.entity.TmTask;
import com.yu.entity.TmTaskDependence;

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

    List<PmProject> searchProjects(String userId,String key);

    List<PieDataParam> getProjectsData(String userId);

    List<String> getStatusData(String userId);

    List<String> getPriorityData(String userId);

    List<TmTaskDependence> getAssociatedTasks(String projectId);

    List<TmTask> getTasksByProjectId(Long project);
}
