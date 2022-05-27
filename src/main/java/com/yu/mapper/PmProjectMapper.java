package com.yu.mapper;

import com.yu.dto.PieDataParam;
import com.yu.entity.PmProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.entity.TmTask;
import com.yu.entity.TmTaskDependence;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author llrem
 * @since 2022-02-20
 */
@Component
public interface PmProjectMapper extends BaseMapper<PmProject> {
    @Select("SELECT pm_project.id,name,create_date,picture,description,create_user_id,status " +
            "FROM pm_project_user,pm_project " +
            "WHERE pm_project_user.project_id = pm_project.id " +
            "AND user_id = #{userId} " +
            "AND name LIKE '%${key}%'")
    List<PmProject> searchProjects(String userId,String key);

    @Select("SELECT status as name,COUNT(status) as value " +
            "FROM pm_project_user,pm_project " +
            "WHERE pm_project_user.project_id = pm_project.id " +
            "AND user_id = #{userId} " +
            "GROUP BY status")
    List<PieDataParam> getProjectsData(String userId);

    @Select("SELECT COUNT(status) " +
            "FROM tm_task_follower, tm_task " +
            "WHERE tm_task_follower.task_id=tm_task.id " +
            "AND user_id = #{userId} " +
            "GROUP BY status " +
            "ORDER BY status ")
    List<String> getStatusData(String userId);

    @Select("SELECT COUNT(priority) " +
            "FROM tm_task_follower, tm_task " +
            "WHERE tm_task_follower.task_id=tm_task.id " +
            "AND user_id = #{userId} " +
            "GROUP BY priority " +
            "ORDER BY priority ")
    List<String> getPriorityData(String userId);

    @Select("SELECT * FROM tm_task_dependence " +
            "WHERE head_task IN (SELECT task_id " +
            "FROM tm_board_task " +
            "WHERE board_id IN (SELECT board_id " +
            "FROM pm_project_board " +
            "WHERE project_id = #{projectId}))")
    List<TmTaskDependence> getAssociatedTasks(String projectId);

    @Select("SELECT * FROM tm_task " +
            "WHERE id IN (SELECT task_id FROM tm_board_task " +
            "WHERE board_id in (SELECT board_id FROM pm_project_board " +
            "WHERE project_id = #{projectId}))")
    List<TmTask> getTasksByProjectId(Long project);
}
