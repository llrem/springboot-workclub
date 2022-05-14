package com.yu.mapper;

import com.yu.dto.SubTaskParam;
import com.yu.entity.TmTaskSub;
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
 * @since 2022-05-02
 */
@Component
public interface TmTaskSubMapper extends BaseMapper<TmTaskSub> {
    @Select("SELECT tm_task_sub.id as id, content, create_date, nick_name, icon " +
            "FROM tm_task_sub,um_user " +
            "WHERE tm_task_sub.user_id = um_user.id " +
            "AND task_id = #{taskId}")
    List<SubTaskParam> getSubTasks(String taskId);
}
