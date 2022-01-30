package com.yu.mapper;

import com.yu.common.UserDetail;
import com.yu.entity.UmUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author llrem
 * @since 2022-01-21
 */
@Repository
public interface UmUserMapper extends BaseMapper<UmUser> {
    UmUser findByUsername(String username);
}
