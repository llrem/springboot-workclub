package com.yu.service;

import com.yu.dto.CardDataParam;
import com.yu.dto.PieDataParam;
import com.yu.entity.TmBoardTask;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llrem
 * @since 2022-03-15
 */
public interface TmBoardTaskService extends IService<TmBoardTask> {
    PieDataParam getBoardData(Long boardId);

    List<CardDataParam> numberOfType(String projectId);

    List<CardDataParam> numberOfStatus(String projectId);

    List<CardDataParam> numberOfPriority(String projectId);
}
