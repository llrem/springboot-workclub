package com.yu.mapper;

import com.yu.dto.TaskLogParam;
import com.yu.entity.TmTaskLog;
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
 * @since 2022-04-01
 */
@Component
public interface TmTaskLogMapper extends BaseMapper<TmTaskLog> {
    @Select("SELECT nick_name as name, object, create_date, type " +
            "FROM tm_task_log, um_user " +
            "WHERE tm_task_log.user_id = um_user.id " +
            "AND tm_task_log.task_id = #{taskId}")
    List<TaskLogParam> getLogsByTaskId(String taskId);
}
