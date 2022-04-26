package com.yu.mapper;

import com.yu.entity.TmTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
 * @since 2022-03-11
 */
@Component
public interface TmTaskMapper extends BaseMapper<TmTask> {
    @Select("SELECT task_id as id, description,start_date,due_date,priority,type,status " +
            "FROM tm_board_task, tm_task " +
            "WHERE tm_board_task.task_id = tm_task.id " +
            "AND tm_board_task.board_id = #{boardId} " +
            "AND description like '%${keyword}%'")
    List<TmTask> getTasksByBoardId(@Param("boardId") String boardId,String keyword);
}
