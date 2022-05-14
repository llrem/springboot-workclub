package com.yu.mapper;

import com.yu.dto.TaskParam;
import com.yu.entity.TmTaskDependence;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author llrem
 * @since 2022-05-03
 */
@Component
public interface TmTaskDependenceMapper extends BaseMapper<TmTaskDependence> {
    @Select("SELECT head_task as id, description, status " +
            "FROM tm_task, tm_task_dependence " +
            "WHERE tm_task_dependence.head_task = tm_task.id " +
            "AND tm_task_dependence.rear_task = #{taskId}")
    List<TaskParam> getHeadTaskList(String taskId);
}
