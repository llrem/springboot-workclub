package com.yu.service.impl;

import com.yu.dto.CardDataParam;
import com.yu.dto.PieDataParam;
import com.yu.entity.TmBoardTask;
import com.yu.mapper.TmBoardTaskMapper;
import com.yu.service.TmBoardTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author llrem
 * @since 2022-03-15
 */
@Service
public class TmBoardTaskServiceImpl extends ServiceImpl<TmBoardTaskMapper, TmBoardTask> implements TmBoardTaskService {
    @Autowired
    TmBoardTaskMapper boardTaskMapper;

    @Override
    public PieDataParam getBoardData(Long boardId) {
        return boardTaskMapper.getBoardData(boardId);
    }

    @Override
    public List<CardDataParam> numberOfType(String projectId) {
        return boardTaskMapper.numberOfType(projectId);
    }

    @Override
    public List<CardDataParam> numberOfStatus(String projectId) {
        return boardTaskMapper.numberOfStatus(projectId);
    }

    @Override
    public List<CardDataParam> numberOfPriority(String projectId) {
        return boardTaskMapper.numberOfPriority(projectId);
    }

}
