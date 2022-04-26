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
    List<MemberParam> getMemberListByProjectId(String projectId);

    @Select("SELECT project_id as id, name, create_date, picture, description, create_user_id, status " +
            "FROM pm_project_user, pm_project " +
            "WHERE pm_project_user.project_id = pm_project.id " +
            "AND pm_project_user.user_id = #{userId}")
    List<PmProject> getProjectsByUserId(String userId);

    //这里要用${}
    @Select("SELECT user_id as id, username, icon, nick_name " +
            "FROM pm_project_user, um_user " +
            "WHERE pm_project_user.user_id = um_user.id " +
            "AND nick_name like '%${keyword}%' " +
            "AND pm_project_user.project_id = #{projectId}")
    List<MemberParam> searchMember(String keyword, String projectId);

}
