package com.yu.mapper;

import com.yu.dto.MemberParam;
import com.yu.dto.PermissionParam;
import com.yu.entity.PmProject;
import com.yu.entity.PmProjectUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author llrem
 * @since 2022-03-22
 */
@Component
public interface PmProjectUserMapper extends BaseMapper<PmProjectUser> {
    @Select("SELECT user_id as id, username, icon, nick_name " +
            "FROM pm_project_user, um_user " +
            "WHERE pm_project_user.user_id = um_user.id " +
            "AND pm_project_user.project_id = #{projectId}")
    List<MemberParam> getMemberListByProjectId(Long projectId);

    @Select("SELECT project_id as id, name, create_date, picture, description, create_user_id, status " +
            "FROM pm_project_user, pm_project " +
            "WHERE pm_project_user.project_id = pm_project.id " +
            "AND pm_project_user.user_id = #{userId}")
    List<PmProject> getProjectsByUserId(Long userId);


    @Select("SELECT name " +
            "FROM um_user_role, um_role " +
            "WHERE um_user_role.role_id = um_role.id " +
            "AND um_user_role.user_id = #{userId}")
    List<String> getPermissionByUserId(Long userId);
}
