package com.yu.mapper;

import com.yu.dto.CommentParam;
import com.yu.entity.TmTaskComment;
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
 * @since 2022-03-26
 */
@Component
public interface TmTaskCommentMapper extends BaseMapper<TmTaskComment> {
    @Select("SELECT  tm_task_comment.id, nick_name as name, icon, create_date, content " +
            "FROM tm_task_comment, um_user " +
            "WHERE tm_task_comment.user_id = um_user.id " +
            "AND tm_task_comment.task_id = #{taskId}")
    List<CommentParam> getCommentsByTaskId(Long taskId);
}
