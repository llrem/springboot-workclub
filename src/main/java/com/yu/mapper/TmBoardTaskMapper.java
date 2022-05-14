package com.yu.mapper;

import com.yu.dto.CardDataParam;
import com.yu.dto.PieDataParam;
import com.yu.entity.TmBoardTask;
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
 * @since 2022-03-15
 */
@Component
public interface TmBoardTaskMapper extends BaseMapper<TmBoardTask> {
    @Select("SELECT name, COUNT(board_id) as value " +
            "FROM tm_board_task, tm_board " +
            "WHERE tm_board_task.board_id = tm_board.id AND board_id=#{boardId}")
    PieDataParam getBoardData(Long boardId);

    @Select("SELECT type as name, COUNT(type) as value " +
            "FROM tm_board_task, tm_task " +
            "WHERE tm_board_task.task_id = tm_task.id " +
            "AND board_id IN (SELECT board_id FROM pm_project_board WHERE project_id = #{projectId})" +
            "GROUP BY type "+
            "ORDER BY type")
    List<CardDataParam> numberOfType(String projectId);

    @Select("SELECT status as name, COUNT(status) as value " +
            "FROM tm_board_task, tm_task " +
            "WHERE tm_board_task.task_id = tm_task.id " +
            "AND board_id IN (SELECT board_id FROM pm_project_board WHERE project_id = #{projectId})" +
            "GROUP BY status "+
            "ORDER BY status")
    List<CardDataParam> numberOfStatus(String projectId);

    @Select("SELECT priority as name, COUNT(priority) as value " +
            "FROM tm_board_task, tm_task " +
            "WHERE tm_board_task.task_id = tm_task.id " +
            "AND board_id IN (SELECT board_id FROM pm_project_board WHERE project_id = #{projectId})" +
            "GROUP BY priority " +
            "ORDER BY priority")
    List<CardDataParam> numberOfPriority(String projectId);

}
