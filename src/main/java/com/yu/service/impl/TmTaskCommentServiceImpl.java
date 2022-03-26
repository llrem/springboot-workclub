package com.yu.service.impl;

import com.yu.dto.CommentParam;
import com.yu.entity.TmTaskComment;
import com.yu.mapper.TmTaskCommentMapper;
import com.yu.service.TmTaskCommentService;
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
 * @since 2022-03-26
 */
@Service
public class TmTaskCommentServiceImpl extends ServiceImpl<TmTaskCommentMapper, TmTaskComment> implements TmTaskCommentService {

    @Autowired
    TmTaskCommentMapper taskCommentMapper;

    @Override
    public List<CommentParam> getCommentsByTaskId(Long taskId) {
        return taskCommentMapper.getCommentsByTaskId(taskId);
    }
}
