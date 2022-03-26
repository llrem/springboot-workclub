package com.yu.service;

import com.yu.dto.CommentParam;
import com.yu.entity.TmTaskComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llrem
 * @since 2022-03-26
 */
public interface TmTaskCommentService extends IService<TmTaskComment> {
    List<CommentParam> getCommentsByTaskId(Long taskId);
}
