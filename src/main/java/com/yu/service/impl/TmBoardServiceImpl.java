package com.yu.service.impl;

import com.yu.entity.TmBoard;
import com.yu.mapper.TmBoardMapper;
import com.yu.service.TmBoardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author llrem
 * @since 2022-03-12
 */
@Service
public class TmBoardServiceImpl extends ServiceImpl<TmBoardMapper, TmBoard> implements TmBoardService {

    @Autowired
    TmBoardMapper boardMapper;

    @Override
    public int saveAndReturnID(TmBoard board) {
        return boardMapper.insert(board);
    }
}
