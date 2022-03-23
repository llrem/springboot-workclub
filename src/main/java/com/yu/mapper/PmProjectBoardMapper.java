package com.yu.mapper;

import com.yu.entity.PmProjectBoard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.entity.TmBoard;
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
 * @since 2022-03-14
 */
@Component
public interface PmProjectBoardMapper extends BaseMapper<PmProjectBoard> {

    @Select("SELECT board_id as id, name " +
            "FROM pm_project_board, tm_board " +
            "WHERE pm_project_board.board_id = tm_board.id " +
            "AND pm_project_board.project_id = #{projectId}")
    List<TmBoard> getBoardsByProjectId(@Param("projectId") String projectId);
}
