package com.yu.service.impl;

import com.yu.entity.PmProjectBoard;
import com.yu.entity.TmBoard;
import com.yu.mapper.PmProjectBoardMapper;
import com.yu.service.PmProjectBoardService;
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
 * @since 2022-03-14
 */
@Service
public class PmProjectBoardServiceImpl extends ServiceImpl<PmProjectBoardMapper, PmProjectBoard> implements PmProjectBoardService {

    @Autowired
    PmProjectBoardMapper projectBoardMapper;

    @Override
    public List<TmBoard> getBoardsByProjectId(String projectId) {
        return projectBoardMapper.getBoardsByProjectId(projectId);
    }
}
