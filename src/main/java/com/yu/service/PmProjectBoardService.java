package com.yu.service;

import com.yu.entity.PmProjectBoard;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yu.entity.TmBoard;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llrem
 * @since 2022-03-14
 */
public interface PmProjectBoardService extends IService<PmProjectBoard> {
    List<TmBoard> getBoardsByProjectId(String projectId);
}
