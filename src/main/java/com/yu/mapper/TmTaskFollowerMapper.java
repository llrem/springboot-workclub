package com.yu.mapper;

import com.yu.dto.MemberParam;
import com.yu.entity.TmTask;
import com.yu.entity.TmTaskFollower;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.entity.UmUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author llrem
 * @since 2022-03-24
 */
@Component
public interface TmTaskFollowerMapper extends BaseMapper<TmTaskFollower> {
    @Select("SELECT user_id as id, username, icon, nick_name " +
            "FROM tm_task_follower, um_user " +
            "WHERE tm_task_follower.user_id = um_user.id " +
            "AND tm_task_follower.task_id = #{taskId}")
    List<MemberParam> getFollowersByTaskId(String taskId);

    @Select("SELECT task_id as id, description,start_date,due_date,priority,type,status " +
            "FROM tm_task_follower, tm_task " +
            "WHERE tm_task_follower.task_id = tm_task.id " +
            "AND tm_task_follower.user_id = #{userId}")
    List<TmTask> getTaskByUserId(@Param("userId") String userId);
}
