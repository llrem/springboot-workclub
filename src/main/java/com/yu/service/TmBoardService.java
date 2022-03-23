package com.yu.service;

import com.yu.entity.TmBoard;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llrem
 * @since 2022-03-12
 */
public interface TmBoardService extends IService<TmBoard> {
    int saveAndReturnID(TmBoard board);
}
